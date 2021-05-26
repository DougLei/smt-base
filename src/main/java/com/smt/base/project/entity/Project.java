package com.smt.base.project.entity;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class Project {
	private String id;
	private String parentId;
	private String rootId;
	private int level;
	private String code;
	private String name;
	private Integer isVirtual; // 是否是虚拟项目, 1是0否
	private State state;
	private String description;
	private String createUserId;
	private Date createDate;
	private String tenantId;
	private String bufferId;
	private String authCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRootId() {
		return rootId;
	}
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIsVirtual() {
		return isVirtual;
	}
	public void setIsVirtual(Integer isVirtual) {
		this.isVirtual = isVirtual;
	}
	public State getStateInstance() {
		return state;
	}
	public void setStateInstance(State state) {
		this.state = state;
	}
	public int getState() {
		return state.getValue();
	}
	public void setState(int state) {
		this.state = State.valueOf(state);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getBufferId() {
		return bufferId;
	}
	public void setBufferId(String bufferId) {
		this.bufferId = bufferId;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
}
