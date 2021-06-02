package com.smt.base.rel;

/**
 * 
 * @author DougLei
 */
public enum Type {
	USER_ID(50, false),
	PROJECT_CODE(40, false),
	ORG_CODE(30, false),
	
	POST_CODE(20, true),
	ROLE_CODE(10, true),
	CUSTOM(0, true); // 自定义
	
	private int level; // key的级别, 值越大, 级别越高; 级别高的数据会作为left, 反之会作为right
	private boolean linkedProject; // 数据值是否和具体的项目关联, 1是0否; 决定了在关系表中的数据, 是否必须在project_code列中存储所属的项目code
	private Type(int level, boolean linkedProject) {
		this.level = level;
		this.linkedProject = linkedProject;
	}
	public int getLevel() {
		return level;
	}
	public boolean getLinkedProject() {
		return linkedProject;
	}
}
