package com.smt.base.rel;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class DataRel {
	private int id;
	private String leftKey;
	private String leftValue;
	private String rightKey;
	private String rightValue;
	private String createUserId;
	private Date createDate;
	private String projectCode;
	private String tenantId;
	
	public DataRel() {}
	public DataRel(String leftKey, String leftValue, String rightKey, String rightValue, String createUserId, Date createDate, String projectCode, String tenantId) {
		this.leftKey = leftKey;
		this.leftValue = leftValue;
		this.rightKey = rightKey;
		this.rightValue = rightValue;
		this.createUserId = createUserId;
		this.createDate = createDate;
		this.projectCode = projectCode;
		this.tenantId = tenantId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLeftKey() {
		return leftKey;
	}
	public void setLeftKey(String leftKey) {
		this.leftKey = leftKey;
	}
	public String getLeftValue() {
		return leftValue;
	}
	public void setLeftValue(String leftValue) {
		this.leftValue = leftValue;
	}
	public String getRightKey() {
		return rightKey;
	}
	public void setRightKey(String rightKey) {
		this.rightKey = rightKey;
	}
	public String getRightValue() {
		return rightValue;
	}
	public void setRightValue(String rightValue) {
		this.rightValue = rightValue;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
