package com.smt.base.org;

import java.util.Arrays;
import java.util.List;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.SmtBaseException;
import com.smt.parent.code.response.Response;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class OrgService {

	// 验证code是否存在
	private boolean codeExists(Org org) {
		return SessionContext.getSqlSession().uniqueQuery_(
				"select id from base_org where code=? and tenant_id=?", 
				Arrays.asList(org.getCode(), org.getTenantId())) != null;
	}
	
	/**
	 * 添加组织机构
	 * @param org
	 * @return
	 */
	@Transaction
	public Response insert(Org org) {
		if(codeExists(org))
			return new Response(org, "code", "已存在编码为[%s]的组织机构/部门", "smt.base.org.fail.code.exists", org.getCode());
		
		if(org.getParentId() != null) {
			Org parent = SessionContext.getSqlSession().uniqueQuery(Org.class, "select * from base_org where id=?", Arrays.asList(org.getParentId()));
			if(parent == null || parent.getIsDeleted() == 1)
				throw new SmtBaseException("保存失败, 不存在id为["+org.getParentId()+"]的父组织机构/部门");
			
			if(parent.getType()==2 && org.getType()==1)
				return new Response(org, "type", "部门下不能设立组织机构", "smt.base.org.fail.org.under.dept");
		}else if(org.getType() == 2) {
			return new Response(org, "type", "部门不能作为组织机构的根节点", "smt.base.org.fail.dept.as.root");
		}
		
		SessionContext.getTableSession().save(org);
		return new Response(org);
	}

	/**
	 * 修改组织机构
	 * @param org
	 * @return
	 */
	@Transaction
	public Response update(Org org) {
		Org old = SessionContext.getSqlSession().uniqueQuery(Org.class, "select * from base_org where id=?", Arrays.asList(org.getId()));
		if(old == null || old.getIsDeleted() == 1)
			throw new SmtBaseException("修改失败, 不存在id为["+org.getId()+"]的组织机构/部门");
		if(org.getParentId() != old.getParentId())
			throw new SmtBaseException("修改失败, 禁止修改关联的父组织机构/部门");
		
		// 判断是否修改了code
		if(!org.getCode().equals(old.getCode()) && codeExists(org))
			return new Response(org, "code", "已存在编码为[%s]的组织机构/部门", "smt.base.org.fail.code.exists", org.getCode());
		
		// 判断是否修改了type
		if(old.getType() ==1 && org.getType()==2) { // 从组织结构改为部门, 要验证子数据中是否有组织机构, 如果存在, 则不能修改
			for (Org children : SessionContext.getSqlSession().query(Org.class, "select * from base_org where parent_id=?", Arrays.asList(org.getId()))) {
				if(children.getType() == 1)
					return new Response(org, "type", "修改失败, 因子数据中存在组织机构类型, 故不能将当前数据改为部门类型", "smt.base.org.update.fail.children.exists.org");
			}
		}else if(org.getParentId() != null && old.getType() ==2 && org.getType()==1) { // 从部门改为组织结构, 要验证父数据是否是部门类型, 如果是, 则不能修改
			Org parent = SessionContext.getSqlSession().uniqueQuery(Org.class, "select * from base_org where id=?", Arrays.asList(org.getParentId()));
			if(parent.getType() == 2)
				return new Response(org, "type", "修改失败, 因父数据为部门类型, 故不能将当前数据改为组织机构类型", "smt.base.org.update.fail.parent.is.dept");
		}
			
		SessionContext.getTableSession().update(org);
		return new Response(org);
	}

	/**
	 * 删除组织机构
	 * @param orgId
	 * @return
	 */
	@Transaction
	public Response delete(int orgId) {
		List<Object> list = Arrays.asList(orgId);
		Org org = SessionContext.getSqlSession().uniqueQuery(Org.class, "select * from base_org where id=?", list);
		if(org == null || org.getIsDeleted() == 1)
			throw new SmtBaseException("删除失败, 不存在id为["+orgId+"]的组织机构/部门");
		
		// 判断是否存在子数据
		int childrenCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from base_org where parent_id=? and is_deleted=0", list)[0].toString());
		if(childrenCount > 0)
			return new Response(orgId, null, "删除失败, 当前组织机构/部门下, 还存在[%d]个子组织机构/部门", "smt.base.org.delete.fail.children.exists", childrenCount);
		
		SessionContext.getSqlSession().executeUpdate("update base_org set is_deleted=1 where id=?", list);
		return new Response(orgId);
	}
}
