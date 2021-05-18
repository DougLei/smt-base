package com.smt.base.role;

import com.smt.base.dict.entity.Dict;

/**
 * 
 * @author DougLei
 */
public class Role extends Dict{
	private String name;
	private int isDeleted;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}
