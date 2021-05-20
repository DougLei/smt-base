package com.smt.base.user.auth;

/**
 * 
 * @author DougLei
 */
public class CLoginAccount {
	private String loginName;
	private String tenantId;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
