package com.smt.base.project.entity;

/**
 * 
 * @author DougLei
 */
public enum State {
	
	/**
	 * 启用: 0
	 */
	ENABLED(0),
	
	/**
	 * 禁用: 1
	 */
	DISABLED(1);
	
	// ---------------------------------------------------------------
	private int value;
	private State(int value) {
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
	 * 根据标识值获取State实例
	 * @param value
	 * @return
	 */
	public static State valueOf(int value) {
		for (State state : State.values()) {
			if(state.value == value)
				return state;
		}
		throw new IllegalArgumentException("不存在value为["+value+"]的State Enum");
	}
}
