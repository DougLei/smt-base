package com.smt.base.user;

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
	private int type;
	private String name;
	private String realName;
	private Integer sex;
	private String phoneNum;
	private String email;
	// 账户信息
	private String loginName;
	private String loginPwd;
	// 关联的信息
	private String orgCode;
	private String projectCode;
	
	// 构建User实例, 设置基础数据
	private User build() {
		User user = new User();
		user.setId(id==null?UUID.randomUUID().toString():id);
		user.setType(type);
		user.setName(name);
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
		user.setTenantId(TokenContext.get().getTenantId());
		return user;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
}
