package com.smt.base.rel;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class DataRelWrapper {
	protected TokenEntity tokenEntity;
	private Integer flag; // 1表示parent的value在left, 0表示parent的value在right
	private Type parentType;
	private String parentValue;
	private Type childType;
	private String childValues;
	
	public DataRelWrapper() {
		this.tokenEntity = TokenContext.get();
	}
	public DataRelWrapper(TokenEntity tokenEntity) {
		this.tokenEntity = tokenEntity;
	}
	
	public Integer getFlag() {
		if(flag == null) 
			flag = parentType.getLevel() >= childType.getLevel()?1:0;
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
	public String getProjectCode() {
		if(parentType.getLinkedProject() || childType.getLinkedProject())
			return tokenEntity.getProjectCode();
		return null;
	}
	public String getTenantId() {
		return tokenEntity.getTenantId();
	}
}
