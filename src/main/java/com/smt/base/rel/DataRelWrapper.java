package com.smt.base.rel;

import com.smt.parent.code.filters.token.TokenContext;

/**
 * 
 * @author DougLei
 */
public class DataRelWrapper {
	private Integer flag; // 1表示parent的value在left, 2表示parent的value在right
	private Type parentType;
	private String parentValue;
	private Type childType;
	private String childValues;
	private String projectCode;
	private String tenantId;
	
	public DataRelWrapper() {
		this.tenantId = TokenContext.get().getTenantId();
	}
	public DataRelWrapper(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public Integer getFlag() {
		if(flag == null) 
			flag = parentType.getLevel() >= childType.getLevel()?1:2;
		return flag;
	}
	public String getParentType() {
		return parentType.name();
	}
	public void setParentType(String parentType) {
		this.parentType = Type.valueOf(parentType.toUpperCase());
	}
	public void setParentTypeInstance(Type parentType) {
		this.parentType = parentType;
	}
	public String getParentValue() {
		return parentValue;
	}
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
	public String getChildType() {
		return childType.name();
	}
	public void setChildType(String childType) {
		this.childType = Type.valueOf(childType.toUpperCase());
	}
	public void setChildTypeInstance(Type childType) {
		this.childType = childType;
	}
	public String getChildValues() {
		return childValues;
	}
	public void setChildValues(String childValues) {
		this.childValues = childValues;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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
}
