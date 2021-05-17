package com.smt.base.project.entity;

/**
 * 
 * @author DougLei
 */
public enum State {
	
	/**
	 * 启用: 1
	 */
	ENABLED(1),
	
	/**
	 * 禁用: 2
	 */
	DISABLED(2),
	
	/**
	 * 删除: 3
	 */
	DELETED(3);
	
	// ---------------------------------------------------------------
	private int value; // 状态值
	private State(int value) {
		this.value = value;
	}

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
