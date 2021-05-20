package com.smt.base.user.auth;

import java.util.HashMap;
import java.util.Map;

import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class TokenContainer {
	private static final Map<String, TokenEntity> CONTAINER = new HashMap<String, TokenEntity>(128);

	/**
	 * 添加token
	 * @param token
	 */
	public static void add(TokenEntity token) {
		CONTAINER.put(token.getValue(), token);
	}
	
	/**
	 * 更新token
	 * @param token
	 */
	public static void update(TokenEntity token) {
		CONTAINER.put(token.getValue(), token);
	}
	
	/**
	 * 获取token
	 * @param token
	 * @return
	 */
	public static TokenEntity get(String token) {
		return CONTAINER.get(token);
	}
	
	/**
	 * 移除token
	 * @param token
	 */
	public static void remove(String token) {
		CONTAINER.remove(token);
	}
}

