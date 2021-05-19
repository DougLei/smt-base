package com.smt.base.link.entity.user_org;

/**
 * 
 * @author DougLei
 */
public class UserOrgLink {
	private int id;
	private String userId;
	private String orgCode;
	private String tenantId;
	
	public UserOrgLink(String userId, String orgCode, String tenantId) {
		this.userId = userId;
		this.orgCode = orgCode;
		this.tenantId = tenantId;
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
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
