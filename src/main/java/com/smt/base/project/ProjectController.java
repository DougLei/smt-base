package com.smt.base.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.base.project.entity.Project;
import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;
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
	
	/**
	 * 插入项目
	 * @param project
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody Project project) {
		TokenEntity token = TokenContext.get();
		project.setCreateUserId(token.getUserId());
		project.setCreateDate(token.getCurrentDate());
		project.setTenantId(token.getTenantId());
		return service.insert(project);
	}

	/**
	 * 修改项目
	 * @param project
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public Response update(@RequestBody Project project) {
		project.setTenantId(TokenContext.get().getTenantId());
		return service.update(project);
	}
	
	/**
	 * 启用项目
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/enable/{projectId}", method=RequestMethod.POST)
	public Response enable(@PathVariable int projectId) {
		return service.enable(projectId);
	}
	
	/**
	 * 禁用项目
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/disable/{projectId}", method=RequestMethod.POST)
	public Response disable(@PathVariable int projectId) {
		return service.disable(projectId);
	}

	/**
	 * 删除项目
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete/{projectId}", method=RequestMethod.DELETE)
	public Response delete(@PathVariable int projectId) {
		return service.delete(projectId);
	}
}
