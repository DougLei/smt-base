package com.smt.base.rel;

import com.smt.parent.code.filters.token.TokenContext;

/**
 * 
 * @author DougLei
 */
public class DataRelWrapper {
	private Integer flag; // 1表示parent的value在left, 2表示parent的value在right
	private Key parentKey;
	private String parentValue;
	private Key childKey;
	private String childValues;
	private String projectCode;
	private String tenantId;
	
	public DataRelWrapper() {
		this(TokenContext.get().getTenantId());
	}
	public DataRelWrapper(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public Integer getFlag() {
		if(flag == null) 
			flag = parentKey.getLevel() >= childKey.getLevel()?1:2;
		return flag;
	}
	public String getParentKey() {
		return parentKey.name();
	}
	public void setParentKey(String parentKey) {
		this.parentKey = Key.valueOf(parentKey.toUpperCase());
	}
	public void setParentKeyInstance(Key parentKey) {
		this.parentKey = parentKey;
	}
	public String getParentValue() {
		return parentValue;
	}
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
	public String getChildKey() {
		return childKey.name();
	}
	public void setChildKey(String childKey) {
		this.childKey = Key.valueOf(childKey.toUpperCase());
	}
	public void setChildKeyInstance(Key childKey) {
		this.childKey = childKey;
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
