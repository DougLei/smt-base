package com.smt.base.project;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author DougLei
 */
@Component
@ConfigurationProperties(prefix="smt.base.project")
public class ProjectConfigurationProperties {
	private int maxLevel; // 项目支持的最大层级; 即可嵌套几层; 默认值为2

	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
}
