package com.smt.base.user.entity;

import java.util.UUID;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class UserBuilder {
	// 用户信息
	private String id;
	private String nikeName;
	private String realName;
	private Integer sex;
	private String phoneNum;
	private String email;
	// 账户信息
	private String loginName;
	private String loginPwd;
	
	// 构建User实例, 设置基础数据
	private User build() {
		User user = new User();
		user.setNikeName(nikeName);
		user.setRealName(realName);
		user.setSex(sex);
		user.setPhoneNum(phoneNum);
		user.setEmail(email);
		return user;
	}
	
	/**
	 * insert时构建User实例
	 * @param openAccount 是否要开通账户
	 * @return
	 */
	public User build4Insert(boolean openAccount) {
		User user = build();
		
		TokenEntity token = TokenContext.get();
		user.setId(UUID.randomUUID().toString());
		user.setCreateUserId(token.getUserId());
		user.setCreateDate(token.getCurrentDate());
		user.setTenantId(token.getTenantId());
		
		if(openAccount) 
			user.setAccount(new AccountBuilder(loginName, loginPwd).build(user));
		return user;
	}
	
	/**
	 * update时构建User实例
	 * @return
	 */
	public User build4Update() {
		User user = build();
		user.setId(id);
		
		return user;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNikeName() {
		return nikeName;
	}
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
}
