package com.smt.base.user.auth;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.douglei.orm.context.PropagationBehavior;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.SmtBaseException;
import com.smt.base.project.ProjectService;
import com.smt.base.project.entity.Project;
import com.smt.base.rel.DataRelService;
import com.smt.base.rel.DataRelWrapper;
import com.smt.base.rel.Type;
import com.smt.base.user.Account;
import com.smt.base.user.User;
import com.smt.base.user.UserService;
import com.smt.parent.code.filters.log.LogContext;
import com.smt.parent.code.filters.token.TokenEntity;
import com.smt.parent.code.response.Response;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class AuthService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataRelService dataRelService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TokenContainer tokenContainer;
	
	/**
	 * 登录
	 * @param entity
	 * @return
	 */
	@Transaction(propagationBehavior=PropagationBehavior.SUPPORTS)
	public Response login(LoginEntity entity) {
		// 进行登录验证
		Account account = SessionContext.getSqlSession().uniqueQuery(Account.class, "select * from base_account where login_name=? and tenant_id=?", Arrays.asList(entity.getLoginName(), entity.getTenantId()));
		if(account == null)
			return new Response(entity, null, "用户名或密码错误", "smt.base.login.fail.name.pwd.error");
		
		// 在日志中记录下用户id
		LogContext.loggingUserId(account.getUserId());
		
		// 对账户进行验证
		if(!account.getLoginPwd().equals(DigestUtils.md5Hex(entity.getLoginPwd()+account.getUserId())))
			return new Response(entity, null, "用户名或密码错误", "smt.base.login.fail.name.pwd.error");
		if(account.getIsDisabled() == 1)
			return new Response(entity, null, "账户已被禁用, 请联系管理员", "smt.base.login.fail.account.disabled");
		
		Date currentDate = new Date();
		if(account.getExpiryDate() != null && account.getExpiryDate().getTime() < currentDate.getTime()) {
			userService.disableAccount_(account.getId(), null, currentDate, "账户过期");
			return new Response(entity, null, "账户已过期, 请联系管理员", "smt.base.login.fail.account.overdue");
		}
		
		// 初始化token实例
		TokenEntity token = new TokenEntity();
		token.setUserId(account.getUserId());
		token.setTenantId(entity.getTenantId());
		
		// 如果ProjectCode不为空时, 验证登录权限, 以及查询用户相关的数据
		if(entity.getProjectCode() != null) {
			Project project = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where code=? and tenant_id=?", Arrays.asList(entity.getProjectCode(), entity.getTenantId()));
			if(project== null)
				throw new SmtBaseException("登录失败, 不存在编码为["+entity.getProjectCode()+"]的项目");
			if(project.getIsVirtual() == 1)
				throw new SmtBaseException("禁止登录编码为["+entity.getProjectCode()+"]的虚拟项目");
			
			// 设置当前项目和其父项目集合; 查询当前的userId是否有权限登录当前的项目
			token.setProjectCode(entity.getProjectCode());
			token.setParentProjectCodes(projectService.getParentCodes(project));
			if(Integer.parseInt(SessionContext.getSQLSession().uniqueQuery_("DataRel", "query4UserIdAndProjectCodes", token)[0].toString()) == 0)
				return new Response(entity, null, "无权登录系统, 请联系管理员", "smt.base.login.fail.no.right");
			
			DataRelWrapper wrapper = new DataRelWrapper(entity.getTenantId());
			wrapper.setParentTypeInstance(Type.USER_ID);
			wrapper.setParentValue(account.getUserId());
			wrapper.setChildTypeInstance(Type.PROJECT_CODE);
			wrapper.setProjectCode(entity.getProjectCode());
			// 查询用户所属的组织机构
			wrapper.setChildTypeInstance(Type.ORG_CODE);
			token.setOrgs(dataRelService.queryValues(wrapper));
			// 查询用户所属的岗位
			wrapper.setChildTypeInstance(Type.POST_CODE);
			token.setPosts(dataRelService.queryValues(wrapper));
			// 查询用户拥有的角色
			wrapper.setChildTypeInstance(Type.ROLE_CODE);
			token.setRoles(dataRelService.queryValues(wrapper));
		}
		
		token.setValue(UUID.randomUUID().toString());
		token.setAccountId(account.getId());
		token.setUserName(SessionContext.getSqlSession().uniqueQuery(User.class, "select name, real_name from base_user where id=?", Arrays.asList(account.getUserId())).getUserName());
		token.setLoginDate(currentDate);
		token.setLastOpDate(currentDate);
		token.setClientType(entity.getClientType());
		token.setClientIp(entity.getClientIp());
		
		// 存储token, 并构建响应体
		tokenContainer.add(token);
		return new Response(token);
	}
	
	// ------------------------------------------------------------------------------------------------------
	/**
	 * 修改token数据
	 * @param entity
	 * @param data
	 * @return
	 */
	@Transaction(propagationBehavior=PropagationBehavior.SUPPORTS)
	public Response updateToken(TokenEntity entity, TokenEntity data) {
		// 验证projectCode
		if(data.getProjectCode() == null && entity.getProjectCode() != null) {
			entity.setProjectCode(null);
			entity.setParentProjectCodes(null);
		}else if(data.getProjectCode() != null && !data.getProjectCode().equals(entity.getProjectCode())) {
			Project project = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where code=? and tenant_id=?", Arrays.asList(data.getProjectCode(), entity.getTenantId()));
			if(project== null)
				throw new SmtBaseException("修改token数据失败, 不存在编码为["+data.getProjectCode()+"]的项目");
			
			entity.setProjectCode(data.getProjectCode());
			entity.setParentProjectCodes(projectService.getParentCodes(project));
		}
		
		entity.setLastOpDate(entity.getCurrentDate());
		entity.setExtend(data.getExtend());
		tokenContainer.update(entity);
		return new Response(entity);
	}
}
