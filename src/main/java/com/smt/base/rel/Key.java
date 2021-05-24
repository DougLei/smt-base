package com.smt.base.rel;

/**
 * 
 * @author DougLei
 */
public enum Key {
	USER_ID(50),
	ORG_CODE(40),
	POST_CODE(30),
	ROLE_CODE(20),
	USER_DEFINED(0); // 用户自定义
	
	private int level; // key的级别, 值越大, 级别越高; 级别高的数据会作为left, 反之会作为right
	private Key(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
}
