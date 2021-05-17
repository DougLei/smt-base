package com.smt.base.user.entity;

import java.util.Arrays;

import org.springframework.util.DigestUtils;

import com.douglei.orm.context.SessionContext;
import com.douglei.tools.StringUtil;
import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class AccountBuilder {
	private int id;
	private String userId;
	private String loginName;
	private String loginPwd;
	private String disableReason;
	
	public AccountBuilder() {}
	public AccountBuilder(int id) {
		this.id = id;
	}
	public AccountBuilder(String loginName, String loginPwd) {
		this.loginName = loginName;
		this.loginPwd = loginPwd;
	}
	
	/**
	 * 根据id获取账户
	 * @return
	 */
	public Account getById() {
		return SessionContext.getSqlSession().uniqueQuery(Account.class, "select * from base_account where id=?", Arrays.asList(id));
	}
	
	/**
	 * 根据用户id获取账户
	 * @return
	 */
	public Account getByUserId() {
		return SessionContext.getSqlSession().uniqueQuery(Account.class, "select * from base_account where user_id=?", Arrays.asList(userId));
	}

	/**
	 * 构建Account实例
	 * @param user
	 * @return
	 */
	public Account build(User user) {
		Account account = new Account();
		account.setLoginName(StringUtil.isEmpty(loginName)?user.getNikeName():loginName);
		account.setLoginPwd(DigestUtils.md5DigestAsHex(((StringUtil.isEmpty(loginPwd)?"111111":loginPwd) + account.getId()).getBytes()));
		account.setUserId(user.getId());
		
		TokenEntity token = TokenContext.get();
		account.setOpenUserId(token.getUserId());
		account.setOpenDate(token.getCurrentDate());
		account.setTenantId(token.getTenantId());
		
		return account;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public String getDisableReason() {
		return disableReason;
	}
	public void setDisableReason(String disableReason) {
		this.disableReason = disableReason;
	}
}
