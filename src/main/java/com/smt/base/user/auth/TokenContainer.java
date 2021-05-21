package com.smt.base.user.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
@Component
public class TokenContainer {
	private final Map<Identity, TokenEntity> container = new HashMap<Identity, TokenEntity>(128);

	/**
	 * 添加token
	 * @param token
	 */
	public void add(TokenEntity token) {
		container.put(new Identity(token), token);
	}
	
	/**
	 * 更新token
	 * @param token
	 */
	public void update(TokenEntity token) {
		container.put(new Identity(token), token);
	}
	
	/**
	 * 获取token
	 * @param token
	 * @return
	 */
	public TokenEntity get(String token) {
		return container.get(new Identity(token));
	}
	
	/**
	 * 移除token
	 * @param token
	 */
	public void remove(String token) {
		container.remove(new Identity(token));
	}
}

/**
 * 
 * @author DougLei
 */
class Identity {
	private String token;
	private String userId;
	
	public Identity(TokenEntity entity) {
		this.token = entity.getValue();
		this.userId = entity.getUserId();
	}
	public Identity(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		return token.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		Identity other = (Identity) obj;
		return token.equals(other.token) || userId.equals(other.userId);
	}
}

