package com.smt.base.user;

import java.util.Arrays;
import java.util.Date;

import org.springframework.util.DigestUtils;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.douglei.tools.StringUtil;
import com.smt.base.SmtBaseException;
import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.response.Response;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class UserService {
	
	// 验证name是否存在
	private boolean nameExists(User user) {
		return SessionContext.getSqlSession().uniqueQuery_(
				"select id from base_user where name=? and tenant_id=?", 
				Arrays.asList(user.getName(), user.getTenantId())) != null;
	}
	// 验证LoginName是否存在
	private boolean loginNameExists(Account account) {
		return SessionContext.getSqlSession().uniqueQuery_(
				"select id from base_account where login_name=? and tenant_id=?", 
				Arrays.asList(account.getLoginName(), account.getTenantId())) != null;
	}
	
	/**
	 * 添加用户
	 * @param builder
	 * @param openAccount
	 * @return
	 */
	@Transaction
	public Response insert(UserBuilder builder, boolean openAccount) {
		User user= builder.build4Insert(openAccount);
		if(nameExists(user))
			return new Response(builder, "name", "名称[%s]已被使用", "smt.base.user.fail.name.exists", user.getName());
		
		// 开通账户
		if(openAccount) {
			Account account = user.getAccount();
			if(loginNameExists(account))
				return new Response(builder, "loginName", "登录名[%s]已被使用", "smt.base.user.fail.loginname.exists", account.getLoginName());
			
			SessionContext.getTableSession().save(account);
		}
		
		SessionContext.getTableSession().save(user);
		return new Response(builder);
	}

	/**
	 * 修改用户
	 * @param builder
	 * @return
	 */
	@Transaction
	public Response update(UserBuilder builder) {
		User old = SessionContext.getTableSession().uniqueQuery(User.class, "select * from base_user where id=?", Arrays.asList(builder.getId()));
		if(old == null || old.getIsDeleted() == 1)
			throw new SmtBaseException("修改失败, 不存在id为["+builder.getId()+"]的用户");
		
		User user = builder.build4Update();
		if(!user.getName().equals(old.getName()) && nameExists(user))
			return new Response(builder, "name", "名称[%s]已被使用", "smt.base.user.fail.name.exists", user.getName());
		
		SessionContext.getTableSession().update(user);
		return new Response(builder);
	}

	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	@Transaction
	public Response delete(String userId) {
		User user = SessionContext.getTableSession().uniqueQuery(User.class, "select * from base_user where id=?", Arrays.asList(userId));
		if(user == null || user.getIsDeleted() == 1)
			throw new SmtBaseException("删除失败, 不存在id为["+userId+"]的用户");
		
		// 如果存在账号, 则将账号删除
		Object[] account = SessionContext.getSqlSession().uniqueQuery_("select id from base_account where user_id=?", Arrays.asList(userId));
		if(account != null)
			SessionContext.getSqlSession().executeUpdate("delete base_account where id=?", Arrays.asList(account[0]));
		
		// 用户信息置于删除状态
		SessionContext.getSqlSession().executeUpdate("update base_user set is_deleted=1 where id=?", Arrays.asList(userId));
		return new Response(userId);
	}
	
	// ------------------------------------------------------------------------------------------------------
	/**
	 * 开通账户
	 * @param builder
	 * @return
	 */
	@Transaction
	public Response openAccount(AccountBuilder builder) {
		User user = SessionContext.getSqlSession().uniqueQuery(User.class, "select * from base_user where id=?", Arrays.asList(builder.getUserId()));
		if(user.getIsDeleted() == 1)
			throw new SmtBaseException("禁止给删除状态的用户["+builder.getUserId()+"]开通账户");
		
		Account account = builder.getByUserId();
		if(account != null) 
			return new Response(builder.getUserId(), null, "用户已开通账户", "smt.base.user.fail.account.opened");
				
		account = builder.build(user);	
		if(loginNameExists(account))
			return new Response(builder.getUserId(), "loginName", "登录名[%s]已被使用", "smt.base.user.fail.loginname.exists", account.getLoginName());
			 
		SessionContext.getTableSession().save(account);
		return new Response(builder.getUserId());
	}
	
	/**
	 * 启用账户
	 * @param builder
	 * @return
	 */
	@Transaction
	public Response enableAccount(AccountBuilder builder) {
		Account account = builder.getById();
		if(account == null)
			throw new SmtBaseException("启用账户失败, 不存在id为["+builder.getId()+"]的账户");
		if(account.getIsDisabled() == 0)
			return new Response(builder.getId(), null, "启用账户失败, 账户处于激活状态", "smt.base.user.fail.account.isenabled");
		
		SessionContext.getSqlSession().executeUpdate("update base_account set is_disabled=0, disable_user_id=null, disable_date=null, disable_reason=null where id=?", Arrays.asList(builder.getId()));
		return new Response(builder.getId());
	}
	
	/**
	 * 禁用账户
	 * @param builder
	 * @return
	 */
	@Transaction
	public Response disableAccount(AccountBuilder builder) {
		Account account = builder.getById();
		if(account == null)
			throw new SmtBaseException("禁用账户失败, 不存在id为["+builder.getId()+"]的账户");
		if(account.getIsDisabled() == 1)
			return new Response(builder.getId(), null, "禁用账户失败, 账户处于禁用状态", "smt.base.user.fail.account.isdisabled");
		
		disableAccount_(builder.getId(), TokenContext.get().getUserId(), new Date(), builder.getDisableReason());
		return new Response(builder.getId());
	}
	
	/**
	 * 禁用账户
	 * @param builder
	 */
	public void disableAccount_(int accountId, String disableUserId, Date disableDate, String disableReason) {
		SessionContext.getSqlSession().executeUpdate(
				"update base_account set is_disabled=1, disable_user_id=?, disable_date=?, disable_reason=? where id=?", 
				Arrays.asList(disableUserId, disableDate, disableReason, accountId));
	}
	
	/**
	 * 删除账户
	 * @param id
	 * @return
	 */
	@Transaction
	public Response deleteAccount(int id) {
		Account account = new AccountBuilder(id).getById();
		if(account == null)
			throw new SmtBaseException("删除账户失败, 不存在id为["+id+"]的账户");
		
		SessionContext.getSqlSession().executeUpdate("delete base_account where id=?", Arrays.asList(id));
		return new Response(id);
	}
	
	/**
	 * 修改账户的登录名和登录密码
	 * @param builder
	 * @return
	 */
	@Transaction
	public Response updateLoginNameAndLoginPwd(AccountBuilder builder) {
		Account account = builder.getById();
		if(account == null)
			throw new SmtBaseException("修改账户的登录名和登录密码失败, 不存在id为["+builder.getId()+"]的账户");
		
		int flag = 0;
		
		// 修改登录名
		if(StringUtil.unEmpty(builder.getLoginName()) && !account.getLoginName().equals(builder.getLoginName())) {
			account.setLoginName(builder.getLoginName());
			
			if(loginNameExists(account))
				return new Response(builder.getUserId(), "loginName", "登录名[%s]已被使用", "smt.base.user.fail.loginname.exists", account.getLoginName());
			flag++;
		}
		
		// 修改登录密码
		if(StringUtil.unEmpty(builder.getLoginPwd())) {
			account.setLoginPwd(DigestUtils.md5DigestAsHex((builder.getLoginPwd() + account.getId()).getBytes()));
			flag++;
		}
		
		if(flag > 0)
			SessionContext.getSqlSession().executeUpdate(
					"update base_account set login_name=?, login_pwd=? where id=?", 
					Arrays.asList(account.getLoginName(), account.getLoginPwd(), account.getId()));
		return new Response(builder.getId()); 
	}
}
