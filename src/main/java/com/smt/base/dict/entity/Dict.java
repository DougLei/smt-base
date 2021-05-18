package com.smt.base.dict.entity;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class Dict {
	private int id;
	private String code;
	private String description;
	private String createUserId;
	private Date createDate;
	private String projectCode;
	private String tenantId;
	private List<DictDetail> details;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public List<DictDetail> getDetails() {
		return details;
	}
	public void setDetails(List<DictDetail> details) {
		this.details = details;
	}
}
