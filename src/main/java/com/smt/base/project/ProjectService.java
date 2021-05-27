package com.smt.base.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.SmtBaseException;
import com.smt.base.project.entity.Project;
import com.smt.base.project.entity.ProjectBuilder;
import com.smt.base.project.entity.State;
import com.smt.parent.code.response.Response;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class ProjectService {
	
	@Autowired
	private ProjectConfigurationProperties properties;
	
	/**
	 * 验证是否存在指定code的项目
	 * @param code
	 * @param tenantId
	 */
	public boolean codeExists(String code, String tenantId) {
		return SessionContext.getSqlSession().uniqueQuery_("select id from base_project where code=? and tenant_id=?", Arrays.asList(code, tenantId)) != null;
	}
	
	/**
	 * 获取指定项目的父项目code集合
	 * @param project
	 * @return 指定的项目为根项目时返回null
	 */
	public List<String> getParentCodes(Project project) {
		if(project.getLevel()== 0)
			return null;
		
		List<String> codes = new ArrayList<String>(project.getLevel());
		recursiveQueryParentCode(codes, project.getLevel()-1, project.getParentId());
		return codes;
	}
	// 递归查询父项目的code
	private void recursiveQueryParentCode(List<String> codes, int deep, Object parentId) {
		Object[] parent = SessionContext.getSQLSession().uniqueQuery_("Project", "queryParentCode", parentId);
		if(parent == null)
			throw new SmtBaseException("递归查询父项目code时, 未能查询到id为["+parentId+"]的父项目");
		codes.add(parent[1].toString());
		
		if(deep ==0)
			return;
		recursiveQueryParentCode(codes, deep--, parent[0]);
	}

	/**
	 * 添加项目
	 * @param builder
	 * @return
	 */
	@Transaction
	public Response insert(ProjectBuilder builder) {
		Project project = builder.build4Insert();
		if(codeExists(project.getCode(), project.getTenantId()))
			return new Response(builder, "code", "已存在编码为[%s]的项目", "smt.base.project.fail.code.exists", project.getCode());
		
		// 计算并设置rootId, level
		if(project.getParentId()== null) {
			project.setRootId(project.getId());
		}else {
			Project parent = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where id=?", Arrays.asList(project.getParentId()));
			if(parent == null || parent.getStateInstance() == State.DELETED)
				throw new SmtBaseException("保存失败, 不存在id为["+project.getParentId()+"]的父项目");
			if(parent.getLevel()+1 >= properties.getMaxLevel())
				return new Response(builder, null, "项目层级不能超过[%d]层", "smt.base.project.insert.fail.level.overflow", properties.getMaxLevel());
			if(parent.getIsVirtual()==0 && project.getIsVirtual()==1)
				return new Response(builder, "type", "实体项目下不能设置虚拟项目", "smt.base.project.fail.virtual.under.entity");
			
			project.setRootId(parent.getRootId());
			project.setLevel(parent.getLevel()+1);
		}
		
		SessionContext.getTableSession().save(project);
		return new Response(builder);
	}
	
	/**
	 * 修改项目
	 * @param builder
	 * @return
	 */
	@Transaction
	public Response update(ProjectBuilder builder) {
		Project old = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where id=?", Arrays.asList(builder.getId()));
		if(old == null || old.getStateInstance() == State.DELETED)
			throw new SmtBaseException("修改失败, 不存在id为["+builder.getId()+"]的项目");
		
		Project project = builder.build4Update(old);
		if(!project.getCode().equals(old.getCode()) && codeExists(project.getCode(), project.getTenantId())) 
			return new Response(builder, "code", "已存在编码为[%s]的项目", "smt.base.project.fail.code.exists", project.getCode());
		if(!ObjectUtils.equals(project.getParentId(), old.getParentId()))
			throw new SmtBaseException("修改失败, 禁止修改项目关联的父项目");
		
		// 判断是否修改了isVirtual
		if(old.getIsVirtual()==1 && project.getIsVirtual()==0) { // 从虚拟改成实体, 要验证子项目是否有虚拟项目, 如果存在, 则不能修改
			for (Project children : SessionContext.getTableSession().query(Project.class, "select * from base_project where parent_id=?", Arrays.asList(project.getId()))) {
				if(children.getIsVirtual() == 1)
					return new Response(builder, "type", "修改失败, 因子项目中存在虚拟项目, 故不能将当前项目改为实体项目", "smt.base.project.update.fail.children.exists.virtual");
			}
		} else if(project.getParentId() != null && old.getIsVirtual()==0 && project.getIsVirtual()==1) { // 从实体改成虚拟, 要验证父项目是否是实体项目, 如果是, 则不能修改
			Project parent = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where id=?", Arrays.asList(project.getParentId()));
			if(parent.getIsVirtual() == 0)
				return new Response(builder, "type", "修改失败, 因父项目为实体项目, 故不能将当前项目改为虚拟项目", "smt.base.project.update.fail.parent.unvirtual");
		}
		
		SessionContext.getTableSession().update(project);
		return new Response(builder);
	}
	
	/**
	 * 启用项目
	 * @param projectId
	 * @return
	 */
	@Transaction
	public Response enable(String projectId) {
		Project project = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where id=?", Arrays.asList(projectId));
		if(project == null || project.getStateInstance() == State.DELETED)
			throw new SmtBaseException("启用失败, 不存在id为["+projectId+"]的项目");
		if(project.getStateInstance() == State.DISABLED)
			SessionContext.getSqlSession().executeUpdate("update base_project set state=? where id=?", Arrays.asList(State.ENABLED.getValue(), projectId));
		return new Response(projectId);
	}
	
	/**
	 * 禁用项目
	 * @param projectId
	 * @return
	 */
	@Transaction
	public Response disable(String projectId) {
		Project project = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where id=?", Arrays.asList(projectId));
		if(project == null || project.getStateInstance() == State.DELETED)
			throw new SmtBaseException("禁用失败, 不存在id为["+projectId+"]的项目");
		
		if(project.getStateInstance() == State.ENABLED) {
			int childrenCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
					"select count(id) from base_project where parent_id=? and state=?", Arrays.asList(projectId, State.ENABLED.getValue()))[0].toString());
			if(childrenCount > 0)
				return new Response(null, null, "项目下存在[%d]个处于启用状态的子项目, 无法禁用", "smt.base.project.disable.fail.children.exists", childrenCount);
			
			SessionContext.getSqlSession().executeUpdate("update base_project set is_default=0, state=? where id=?", Arrays.asList(State.DISABLED.getValue(), projectId));
		}
		return new Response(projectId);
	}
	
	/**
	 * 删除项目
	 * @param projectId
	 * @return
	 */
	@Transaction
	public Response delete(String projectId) {
		Project project = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where id=?", Arrays.asList(projectId));
		if(project == null || project.getStateInstance() == State.DELETED)
			throw new SmtBaseException("删除失败, 不存在id为["+projectId+"]的项目");
		if(project.getStateInstance() == State.ENABLED)
			return new Response(null, null, "项目未处于禁用状态, 无法删除", "smt.base.project.delete.fail.state.error");
		
		// 递归获取子项目id, 统一删除
		List<Object> ids = new ArrayList<Object>();
		ids.add(projectId);
		recursiveQueryChildrenIds(SessionContext.getSqlSession().query_("select id from base_project where parent_id=?", Arrays.asList(projectId)), ids);
		SessionContext.getSQLSession().executeUpdate("Project", "deleteProject", ids);
		return new Response(projectId);
	}
	// 递归查询子项目的id集合
	private void recursiveQueryChildrenIds(List<Object[]> childrenIds, List<Object> ids) {
		if(childrenIds.isEmpty())
			return;
		
		childrenIds.forEach(array -> {
			if(ids.contains(array[0]))
				throw new SmtBaseException("递归查询子项目时, 出现重复的id值["+array[0]+"]");
			ids.add(array[0]);
		});
		recursiveQueryChildrenIds(SessionContext.getSQLSession().query_("Project", "queryChildrenIds", childrenIds), ids);
	}

	/**
	 * 设置默认项目
	 * @param projectId
	 * @return
	 */
	@Transaction
	public Response setDefault(String projectId) {
		Project project = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where id=?", Arrays.asList(projectId));
		if(project == null || project.getStateInstance() == State.DELETED)
			throw new SmtBaseException("设置默认项目失败, 不存在id为["+projectId+"]的项目");
		if(project.getStateInstance() == State.DISABLED)
			return new Response(projectId, null, "项目处于禁用状态, 无法设置为默认项目", "smt.base.project.set.default.fail.state.error");
		
		if(project.getIsDefault() == 0) {
			SessionContext.getSqlSession().executeUpdate("update base_project set is_default=0 where root_id=?", Arrays.asList(project.getRootId()));
			SessionContext.getSqlSession().executeUpdate("update base_project set is_default=1 where id=?", Arrays.asList(projectId));
		}
		return new Response(projectId);
	}

	/**
	 * 取消默认项目
	 * @param projectId
	 * @return
	 */
	@Transaction
	public Response setUnDefault(String projectId) {
		Project project = SessionContext.getTableSession().uniqueQuery(Project.class, "select * from base_project where id=?", Arrays.asList(projectId));
		if(project == null || project.getStateInstance() == State.DELETED)
			throw new SmtBaseException("取消默认项目失败, 不存在id为["+projectId+"]的项目");
		if(project.getStateInstance() == State.DISABLED)
			throw new SmtBaseException("取消默认项目失败, id为["+projectId+"]的项目处于禁用状态");
		
		if(project.getIsDefault() == 1)
			SessionContext.getSqlSession().executeUpdate("update base_project set is_default=0 where id=?", Arrays.asList(projectId));
		return new Response(projectId);
	}
}
