package com.smt.base.project.entity;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class Project {
	private int id;
	private Integer parentId;
	private Integer rootId;
	private int level;
	private String code;
	private String name;
	private int type; // 项目类型: 1(虚拟项目), 2(web项目), 3(触摸屏项目)
	private State state;
	private String description;
	private String createUserId;
	private Date createDate;
	private String tenantId;
	private String bufferId;
	private String authCode;
	
	/**
	 * 是否是实体项目
	 * @return
	 */
	public boolean isEntity() {	
		return type > 1;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getRootId() {
		return rootId;
	}
	public void setRootId(Integer rootId) {
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
