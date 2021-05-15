package com.smt.base.project.entity;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class ProjectBuilder {
	private Integer id;
	private Integer parentId;
	private String code;
	private String name;
	private Integer type;
	private String description;
	
	// 构建Project实例, 设置基础数据
	private Project build() {
		Project project = new Project();
		project.setParentId(parentId);
		project.setCode(code);
		project.setName(name);
		project.setType(type);
		project.setDescription(description);
		
		return project;
	}
	
	/**
	 * 
	 * @return
	 */
	public Project build4Insert() {
		Project project =build();
		
		TokenEntity token = TokenContext.get();
		project.setStateInstance(State.ENABLED);
		project.setCreateUserId(token.getUserId());
		project.setCreateDate(token.getCurrentDate());
		project.setTenantId(token.getTenantId());
		return project;
	}
	
	/**
	 * 
	 * @param old
	 * @return
	 */
	public Project build4Update(Project old) {
		Project project =build();
		project.setId(id);
		
		project.setRootId(old.getRootId());
		project.setStateInstance(old.getStateInstance());
		project.setTenantId(TokenContext.get().getTenantId());
		return project;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
