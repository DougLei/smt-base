package com.smt.base.project;

import java.util.Arrays;
import java.util.UUID;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.project.entity.Project;
import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.response.Response;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class ProjectService {
	
	/**
	 * 
	 * @param project
	 * @return
	 */
	@Transaction
	public Response insert(Project project) {
		project.setCreateUserId(TokenContext.get().getUserId());
		project.setCreateDate(TokenContext.get().getCurrentDate());
		project.setTenantId(TokenContext.get().getTenantId());
		project.setAuthCode(UUID.randomUUID().toString());
		SessionContext.getTableSession().save(project);
		return new Response(project);
	}
	
	/**
	 * 
	 * @param project
	 * @return
	 */
	@Transaction
	public Response update(Project project) {
		SessionContext.getTableSession().update(project);
		return new Response(project);
	}
	
	/**
	 * 
	 * @param projectId
	 * @return
	 */
	@Transaction
	public Response delete(int projectId) {
		SessionContext.getSqlSession().executeUpdate("delete smt_project where id=?", Arrays.asList(projectId));
		return new Response(projectId);
	}
}
