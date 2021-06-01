package com.smt.base.project;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.base.project.entity.ProjectBuilder;
import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.query.QueryExecutor;
import com.smt.parent.code.response.Response;
import com.smt.parent.code.spring.web.LoggingResponse;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ProjectService service;
	
	@Autowired
	private QueryExecutor queryExecutor;
	
	/**
	 * 项目查询
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/query", method=RequestMethod.GET)
	public Response query(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("queryDeleted", "true".equalsIgnoreCase(request.getParameter("queryDeleted")));
		params.put("tenantId", TokenContext.get().getTenantId());
		
		return queryExecutor.execute("QueryProjectList", params, request, "queryDeleted");
	}
	
	/**
	 * 根据项目code和组织机构code, 查询与项目code有关联的用户id集合(用,分隔开)
	 * @param name
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/org/userids/query", method=RequestMethod.GET)
	public Response queryOrgUserids(HttpServletRequest request) {
		return service.queryOrgUserids(request);
	} 
	
	/**
	 * 插入项目
	 * @param builder
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody ProjectBuilder builder) {
		return service.insert(builder);
	}

	/**
	 * 修改项目
	 * @param builder
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public Response update(@RequestBody ProjectBuilder builder) {
		return service.update(builder);
	}
	
	/**
	 * 启用项目
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/enable/{projectId}", method=RequestMethod.POST)
	public Response enable(@PathVariable String projectId) {
		return service.enable(projectId);
	}
	
	/**
	 * 禁用项目
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/disable/{projectId}", method=RequestMethod.POST)
	public Response disable(@PathVariable String projectId) {
		return service.disable(projectId);
	}

	/**
	 * 删除项目
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete/{projectId}", method=RequestMethod.DELETE)
	public Response delete(@PathVariable String projectId) {
		return service.delete(projectId);
	}
	
	/**
	 * 设置默认项目
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/set/default/{projectId}", method=RequestMethod.POST)
	public Response setDefault(@PathVariable String projectId) {
		return service.setDefault(projectId);
	}
	
	/**
	 * 取消默认项目
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/set/undefault/{projectId}", method=RequestMethod.POST)
	public Response setUnDefault(@PathVariable String projectId) {
		return service.setUnDefault(projectId);
	}
}
