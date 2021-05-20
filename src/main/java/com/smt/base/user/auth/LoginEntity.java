package com.smt.base.user.auth;

import com.smt.base.user.auth.temp.TenantId;

/**
 * 
 * @author DougLei
 */
public class LoginEntity {
	private String loginName;
	private String loginPwd;
	private String projectCode;
	private String tenantId = TenantId.TEMP_VALUE;
	private String clientIp; // 客户端ip
	
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
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getTenantId() {
		return tenantId;
	}
//	目前不需要传入租户id信息, 使用默认值即可	
//	public void setTenantId(String tenantId) {
//		this.tenantId = tenantId;
//	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
}
