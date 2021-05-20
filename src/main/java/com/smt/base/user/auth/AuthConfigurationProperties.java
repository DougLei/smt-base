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
	private boolean enableIpLimit = true; // 是否启用客户端ip限制, 即将token和ip进行绑定, 默认值为true
	private long tokenValidTimes = 180000; // token的有效期, 单位为分钟, 默认值为30
	private CLoginAccount[] cloginAccounts; // 可登录配置系统的账户, 不配置时, 任何账户都不能登录配置系统

	public boolean isEnableIpLimit() {
		return enableIpLimit;
	}
	public void setEnableIpLimit(boolean enableIpLimit) {
		this.enableIpLimit = enableIpLimit;
	}
	public long getTokenValidTimes() {
		return tokenValidTimes;
	}
	public int getTokenValidTime() {
		throw new IllegalArgumentException("unsupport this method");
	}
	public void setTokenValidTime(int tokenValidTime) {
		this.tokenValidTimes = tokenValidTime*6000;
	}
	public CLoginAccount[] getCloginAccounts() {
		return cloginAccounts;
	}
	public void setCloginAccounts(CLoginAccount[] cloginAccounts) {
		this.cloginAccounts = cloginAccounts;
	}
}
