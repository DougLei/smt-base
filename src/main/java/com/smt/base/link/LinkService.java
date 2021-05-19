package com.smt.base.link;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.SmtBaseException;
import com.smt.base.link.entity.user_org.UserOrgLink;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class LinkService {
	
	// 获取指定组织机构下的用户id集合
	private Set<String> getUserIdsByOrgCode(UserOrgLink link) {
		List<Object> parameters = Arrays.asList(link.getOrgCode(), link.getTenantId());
		if(SessionContext.getSqlSession().uniqueQuery_("select id from base_org where code=? and tenant_id=?", parameters) == null)
			throw new SmtBaseException("不存在编码为["+link.getOrgCode()+"]的组织机构");
		
		List<Object[]> results = SessionContext.getSqlSession().query_(
				"select user_id from base_user_org_link where org_code=? and tenant_id=?", parameters);
		
		if(results.isEmpty())
			return Collections.emptySet();
		
		HashSet<String> userIds = new HashSet<String>(16);
		results.forEach(result -> userIds.add(result[0].toString()));
		return userIds;
	}
	
	/**
	 * 添加用户和组织机构的关系
	 * @param links
	 */
	@Transaction
	public void insert4UserAndOrgLink(List<UserOrgLink> links) {
		Set<String> existsUserIds = getUserIdsByOrgCode(links.get(0));
		if(!existsUserIds.isEmpty()) {
			for(int i=0; i<links.size();i++) {
				if(existsUserIds.contains(links.get(i).getUserId())) 
					links.remove(i--);
			}
			
			if(links.isEmpty())
				return;
		}
		SessionContext.getTableSession().save(links);
	}

	/**
	 * 删除用户和组织机构的关系
	 * @param links
	 */
	@Transaction
	public void delete4UserAndOrgLink(List<UserOrgLink> links) {
		Set<String> existsUserIds = getUserIdsByOrgCode(links.get(0));
		if(existsUserIds.isEmpty())
			return;
		
		for(int i=0; i<links.size();i++) {
			if(!existsUserIds.contains(links.get(i).getUserId())) 
				links.remove(i--);
		}
		if(links.isEmpty())
			return;
		
		SessionContext.getSQLSession().executeUpdate("Link", "delete4UserAndOrgLink", links);
	}

	/**
	 * 清空指定组织机构下的用户
	 * @param orgCode
	 * @param tenantId
	 */
	@Transaction
	public void clear4UserAndOrgLink(String orgCode, String tenantId) {
		if(getUserIdsByOrgCode(new UserOrgLink(null, orgCode, tenantId)).isEmpty())
			return;
		
		SessionContext.getSqlSession().executeUpdate(
				"delete base_user_org_link where org_code=? and tenant_id=?", Arrays.asList(orgCode, tenantId));
	}
}
