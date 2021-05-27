package com.smt.base.project.entity;

import java.util.UUID;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class ProjectBuilder {
	private String id;
	private String parentId;
	private String code;
	private String name;
	private Integer isVirtual; // 是否虚拟项目, 1是0否
	private String description;
	
	// 构建Project实例, 设置基础数据
	private Project build() {
		Project project = new Project();
		project.setId(id==null?UUID.randomUUID().toString():id);
		project.setParentId(parentId);
		project.setCode(code);
		project.setName(name);
		project.setIsVirtual(isVirtual);
		project.setDescription(description);
		return project;
	}
	
	/**
	 * insert时构建Project实例
	 * @return
	 */
	public Project build4Insert() {
		Project project = build();
		
		TokenEntity token = TokenContext.get();
		project.setStateInstance(State.ENABLED);
		project.setCreateUserId(token.getUserId());
		project.setCreateDate(token.getCurrentDate());
		project.setTenantId(token.getTenantId());
		return project;
	}
	
	/**
	 * update时构建Project实例
	 * @param old
	 * @return
	 */
	public Project build4Update(Project old) {
		Project project = build();
		
		project.setStateInstance(old.getStateInstance());
		project.setTenantId(TokenContext.get().getTenantId());
		return project;
	}
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
