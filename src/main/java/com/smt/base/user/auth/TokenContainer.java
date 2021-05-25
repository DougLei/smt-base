package com.smt.base.user.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.douglei.tools.ExceptionUtil;
import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class TokenContainer {
	private static final Logger logger = LoggerFactory.getLogger(TokenContainer.class);
	private boolean GCJobStarted;
	private final Map<String, String> token_user_container = new ConcurrentHashMap<String, String>(128);
	private final Map<String, List<TokenEntity>> user_tokens_container = new ConcurrentHashMap<String, List<TokenEntity>>(128);
	
	@Autowired
	private AuthConfigurationProperties properties;
	
	/**
	 * 添加token
	 * @param entity
	 */
	public void add(TokenEntity entity) {
		List<TokenEntity> entities = user_tokens_container.get(entity.getUserId());
		if(entities == null) {
			entities = new ArrayList<TokenEntity>();
			user_tokens_container.put(entity.getUserId(), entities);
		}
		
		// 如果当前用户已经有TokenEntity
		if(entities.size() > 0) {
			// 如果启用客户端类型限制, 需要将同clientType的数据移除 
			if(properties.isEnableClientTypeLimit()) {
				for(int i=0; i<entities.size(); i++) {
					if(entities.get(i).getClientType() == entity.getClientType()) {
						token_user_container.remove(entities.remove(i).getValue());
						break;
					}
				}
			}
			// 否则需要将同clientType和clientIp的数据移除 
			else {
				for(int i=0; i<entities.size(); i++) {
					if(entities.get(i).getClientType() == entity.getClientType() && entities.get(i).getClientIp().equals(entity.getClientIp())) {
						token_user_container.remove(entities.remove(i).getValue());
						break;
					}
				}
			}
		}
		
		entities.add(entity);
		token_user_container.put(entity.getValue(), entity.getUserId());
	}
	
	/**
	 * 更新token
	 * @param entity
	 */
	public void update(TokenEntity entity) {
		List<TokenEntity> entities = user_tokens_container.get(entity.getUserId());
		for(int i=0; i<entities.size(); i++) {
			if(entities.get(i).getValue().equals(entity.getValue())) {
				entities.set(i, entity);
				return;
			}
		}
	}
	
	/**
	 * 获取token
	 * @param token
	 * @return
	 */
	public TokenEntity get(String token) {
		String userId = token_user_container.get(token);
		if(userId != null) {
			for(TokenEntity entity: user_tokens_container.get(userId)) {
				if(entity.getValue().equals(token))
					return entity;
			}
		}
		return null;
	}
	
	/**
	 * 移除token
	 * @param token
	 */
	public void remove(String token) {
		String userId= token_user_container.remove(token);
		List<TokenEntity> entities = user_tokens_container.get(userId);
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).getValue().equals(token)) {
				entities.remove(i);
				break;
			}
		}
		
		if(entities.isEmpty())
			user_tokens_container.remove(userId);
	}
	
	/**
	 * 移除token
	 * @param userId
	 */
	public void removeByUserId(String userId) {
		user_tokens_container.remove(userId).forEach(entity -> token_user_container.remove(entity.getValue()));
	}
	
	/**
	 * 执行Token的垃圾回收操作
	 */
	public void execGC() {
		if(user_tokens_container.isEmpty())
			return;
		
		List<String> removeUserIds = new ArrayList<String>(); // user_tokens_container中要移除的userId
		long currentTime = new Date().getTime();
		for(Entry<String, List<TokenEntity>> entry: user_tokens_container.entrySet()) {
			List<TokenEntity> list = entry.getValue();
			
			for(int i=0; i<list.size(); i++) {
				if(list.get(i).getLastOpDate().getTime()+properties.getTokenValidTimes() < currentTime) 
					list.remove(i--);
			}
			if(list.isEmpty())
				removeUserIds.add(entry.getKey());
		}
		
		if(removeUserIds.size() > 0)
			removeUserIds.forEach(userId -> user_tokens_container.remove(userId));
	}
	
	/**
	 * 启动Token的垃圾回收任务
	 */
	public void startGCJob() {
		if(GCJobStarted)
			return;
		
		new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(2*3600000); // 2小时
					} catch (InterruptedException e) {
						logger.error("Token的垃圾回收任务在sleep时出现异常: {}", ExceptionUtil.getStackTrace(e));
					}
					execGC();
				}
			}
		}.start();
		GCJobStarted = true;
		logger.info("启动Token的垃圾回收任务");
	}
}
