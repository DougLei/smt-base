package com.smt.base.rel;

/**
 * 
 * @author DougLei
 */
public class DataRel {
	private int id;
	private String leftType;
	private String leftValue;
	private String rightType;
	private String rightValue;
	private String projectCode;
	private String tenantId;
	
	public DataRel() {}
	public DataRel(String leftType, String leftValue, String rightType, String rightValue, String projectCode, String tenantId) {
		this.leftType = leftType;
		this.leftValue = leftValue;
		this.rightType = rightType;
		this.rightValue = rightValue;
		this.projectCode = projectCode;
		this.tenantId = tenantId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLeftType() {
		return leftType;
	}
	public void setLeftType(String leftType) {
		this.leftType = leftType;
	}
	public String getLeftValue() {
		return leftValue;
	}
	public void setLeftValue(String leftValue) {
		this.leftValue = leftValue;
	}
	public String getRightType() {
		return rightType;
	}
	public void setRightType(String rightType) {
		this.rightType = rightType;
	}
	public String getRightValue() {
		return rightValue;
	}
	public void setRightValue(String rightValue) {
		this.rightValue = rightValue;
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
