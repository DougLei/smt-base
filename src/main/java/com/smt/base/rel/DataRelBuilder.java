package com.smt.base.rel;

import java.util.ArrayList;
import java.util.List;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class DataRelBuilder {
	private Key leftKey;
	private String leftValue;
	private Key rightKey;
	private String rightValues;
	private String projectCode;
	
	/**
	 * 构建数据关联关系集合
	 * @return
	 */
	public List<DataRel> build() {
		TokenEntity token = TokenContext.get();
		
		List<DataRel> list = new ArrayList<DataRel>();
		for(String rightValue: rightValues.split(","))
			list.add(new DataRel(leftKey.name(), leftValue, rightKey.name(), rightValue, token.getUserId(), token.getCurrentDate(), projectCode, token.getTenantId()));
		return list;
	}
	
	public String getLeftKey() {
		return leftKey.name();
	}
	public void setLeftKey(String leftKey) {
		this.leftKey = Key.valueOf(leftKey.toUpperCase());
	}
	public String getRightKey() {
		return rightKey.name();
	}
	public void setRightKey(String rightKey) {
		this.leftKey = Key.valueOf(rightKey.toUpperCase());
	}
	public String getLeftValue() {
		return leftValue;
	}
	public void setLeftValue(String leftValue) {
		this.leftValue = leftValue;
	}
	public String getRightValues() {
		return rightValues;
	}
	public void setRightValues(String rightValues) {
		this.rightValues = rightValues;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
}
