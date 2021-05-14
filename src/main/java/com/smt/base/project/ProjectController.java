package com.smt.base.project;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.base.project.entity.Project;
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
	 * 插入项目信息
	 * @param project
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody Project project) {
		return service.insert(project);
	}

	/**
	 * 修改项目信息
	 * @param project
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public Response update(@RequestBody Project project) {
		return service.update(project);
	}

	/**
	 * 删除项目信息
	 * @param projectId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete/{projectId}", method=RequestMethod.DELETE)
	public Response delete(@PathVariable int projectId) {
		return service.delete(projectId);
	}
}
