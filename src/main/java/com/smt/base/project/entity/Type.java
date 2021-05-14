package com.smt.base.project.entity;

/**
 * 
 * @author DougLei
 */
public enum Type {
	
	/**
	 * 虚拟项目: 0
	 */
	VIRTUAL(0),
	
	/**
	 * Web项目: 1
	 */
	WEB(1),
	
	/**
	 * 触摸屏项目: 2
	 */
	TOUCH_SCREEN(2);
	
	// ---------------------------------------------------------------
	private int value;
	private Type(int value) {
		this.value = value;
	}
	
	/**
	 * 获取标识值
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 根据标识值获取Type实例
	 * @param value
	 * @return
	 */
	public static Type valueOf(int value) {
		for (Type type : Type.values()) {
			if(type.value == value)
				return type;
		}
		throw new IllegalArgumentException("不存在value为["+value+"]的Type Enum");
	}
}
