package com.smt.base.user.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author DougLei
 */
@Component
@ConfigurationProperties(prefix="smt.base.auth")
public class AuthConfigurationProperties {
	private boolean enableClientTypeLimit = true; // 是否启用客户端类型限制, 即用户在同一种的clientType下, 只能有一条有效的token数据, 默认值为true
	private long tokenValidTimes = 180000; // token的有效期, 单位为毫秒, 默认值为180000(30分钟)
	private CLoginAccount[] cloginAccounts; // 可登录配置系统的账户, 不配置时, 任何账户都不能登录配置系统

	public boolean isEnableClientTypeLimit() {
		return enableClientTypeLimit;
	}
	public void setEnableClientTypeLimit(boolean enableClientTypeLimit) {
		this.enableClientTypeLimit = enableClientTypeLimit;
	}
	public long getTokenValidTimes() {
		return tokenValidTimes;
	}
	public int getTokenValidMinute() {
		throw new IllegalArgumentException("unsupport this method");
	}
	public void setTokenValidMinute(int tokenValidMinute) {
		this.tokenValidTimes = tokenValidMinute*60000;
	}
	public CLoginAccount[] getCloginAccounts() {
		return cloginAccounts;
	}
	public void setCloginAccounts(CLoginAccount[] cloginAccounts) {
		this.cloginAccounts = cloginAccounts;
	}
}
