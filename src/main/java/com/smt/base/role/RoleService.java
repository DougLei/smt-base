package com.smt.base.role;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.SmtBaseException;
import com.smt.base.rel.DataRelService;
import com.smt.base.rel.Type;
import com.smt.parent.code.response.Response;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class RoleService {

	@Autowired
	private DataRelService dataRelService;
	
	// 验证code是否存在
	private boolean codeExists(Role role) {
		return SessionContext.getSqlSession().uniqueQuery_(
				"select id from base_role where code=? and tenant_id=?", 
				Arrays.asList(role.getCode(), role.getTenantId())) != null;
	}
	
	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	@Transaction
	public Response insert(Role role) {
		if(codeExists(role))
			return new Response(role, "code", "已存在编码为[%s]的角色", "smt.base.role.fail.code.exists", role.getCode());
		
		SessionContext.getTableSession().save(role);
		return new Response(role);
	}

	/**
	 * 修改角色
	 * @param role
	 * @return
	 */
	@Transaction
	public Response update(Role role) {
		Role old = SessionContext.getSqlSession().uniqueQuery(Role.class, "select * from base_role where id=?", Arrays.asList(role.getId()));
		if(old == null || old.getIsDeleted() == 1)
			throw new SmtBaseException("修改失败, 不存在id为["+role.getId()+"]的角色");
		
		// 判断是否修改了code
		if(!role.getCode().equals(old.getCode()) && codeExists(role))
			return new Response(role, "code", "已存在编码为[%s]的角色", "smt.base.role.fail.code.exists", role.getCode());
		
		SessionContext.getTableSession().update(role);
		return new Response(role);
	}

	/**
	 * 删除角色
	 * @param roleId
	 * @return
	 */
	@Transaction
	public Response delete(int roleId) {
		Role role = SessionContext.getSqlSession().uniqueQuery(Role.class, "select * from base_role where id=?", Arrays.asList(roleId));
		if(role == null || role.getIsDeleted() == 1)
			throw new SmtBaseException("删除失败, 不存在id为["+roleId+"]的角色");
		
		SessionContext.getSqlSession().executeUpdate("update base_role set is_deleted=1 where id=?", Arrays.asList(roleId));
		dataRelService.deleteAll(Type.ROLE_CODE, role.getCode());
		return new Response(roleId);
	}
}
