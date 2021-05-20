package com.smt.base.user.auth;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.rel.DataRel;
import com.smt.base.rel.Key;
import com.smt.base.user.Account;
import com.smt.base.user.UserService;
import com.smt.base.user.auth.temp.TenantId;
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
	
	/**
	 * 登录
	 * @param entity
	 * @return
	 */
	@Transaction
	public Response login(LoginEntity entity) {
		if(!entity.getTenantId().equals(TenantId.TEMP_VALUE))
			return new Response(entity, "tenantId", "不存在相关的租户信息", "smt.base.login.fail.tenantId.unexists");
		
		Account account = SessionContext.getSqlSession().uniqueQuery(Account.class, "select * from base_account where login_name=? and tenant_id=?", Arrays.asList(entity.getLoginName(), entity.getTenantId()));
		if(account == null || !account.getLoginPwd().equals(DigestUtils.md5Hex(entity.getLoginPwd()+account.getUserId())))
			return new Response(entity, null, "用户名或密码错误", "smt.base.login.fail.name.pwd.error");
		if(account.getIsDisabled() == 1)
			return new Response(entity, null, "账户已被禁用, 请联系管理员", "smt.base.login.fail.account.disabled");
		
		Date currentDate = new Date();
		if(account.getExpiryDate() != null && account.getExpiryDate().getTime() < currentDate.getTime()) {
			userService.disableAccount_(account.getId(), null, currentDate, "账户过期");
			return new Response(entity, null, "账户已过期, 请联系管理员", "smt.base.login.fail.account.overdue");
		}
		
		// 创建token, 以及查询相关的数据
		TokenEntity token = new TokenEntity();
		token.setValue(UUID.randomUUID().toString());
		token.setAccountId(account.getId());
		token.setUserId(account.getUserId());
		
		// 如果ProjectCode不为空时,  查询用户相关的数据
		if(entity.getProjectCode() != null) {
			token.setProjectCode(entity.getProjectCode());
			
			DataRel rel = new DataRel();
			rel.setLeftKey(Key.USER_ID.name());
			rel.setLeftValue(account.getUserId());
			rel.setProjectCode(entity.getProjectCode());
			rel.setTenantId(entity.getTenantId());
			
			token.setOrgs(getRightValuesByUserId(rel, Key.ORG_CODE));
			token.setRoles(getRightValuesByUserId(rel, Key.ROLE_CODE));
			token.setPosts(getRightValuesByUserId(rel, Key.POST_CODE));
		}
		
		token.setTenantId(entity.getTenantId());
		token.setLoginDate(currentDate);
		token.setLastOpDate(currentDate);
		token.setClientIp(entity.getClientIp());
		
		// 存储token, 并构建响应体
		TokenContainer.add(token);
		return new Response(token);
	}
	private String[] getRightValuesByUserId(DataRel rel, Key rightKey) {
		rel.setRightKey(rightKey.name());
		
		List<Object[]> results = SessionContext.getSQLSession().query_("Auth", "getRightValues", rel);
		if(results.isEmpty())
			return null;
		
		String[] rightValues = new String[results.size()];
		for (int i = 0; i < results.size(); i++) 
			rightValues[i] = results.get(i)[0].toString();
		return rightValues;
	}
	
	
}
