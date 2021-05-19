package com.smt.base.link.entity.user_org;

import java.util.ArrayList;
import java.util.List;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;

/**
 * 
 * @author DougLei
 */
public class UserOrgLinkBuilder {
	private String orgCode;
	private List<String> userIds;
	
	/**
	 * 构建[一个组织机构:多个用户]的关系集合
	 * @return
	 */
	public List<UserOrgLink> build() {
		TokenEntity token = TokenContext.get();
		
		List<UserOrgLink> links = new ArrayList<UserOrgLink>(userIds.size());
		userIds.forEach(userId -> links.add(new UserOrgLink(userId, orgCode, token.getTenantId())));
		return links;
	}
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public List<String> getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = new ArrayList<String>();
		
		for(String userId: userIds.split(","))
			this.userIds.add(userId);
	}
}
