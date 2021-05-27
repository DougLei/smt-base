package com.smt.base.role;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;
import com.smt.parent.code.query.QueryExecutor;
import com.smt.parent.code.response.Response;
import com.smt.parent.code.spring.web.LoggingResponse;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService service;
	
	@Autowired
	private QueryExecutor queryExecutor;
	
	/**
	 * 角色查询
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/query", method=RequestMethod.GET)
	public Response query(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("queryDeleted", "true".equalsIgnoreCase(request.getParameter("queryDeleted")));
		params.put("token", TokenContext.get().getTenantId());
		
		return queryExecutor.execute("QueryRoleList", params, request);
	}
	
	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody Role role) {
		TokenEntity token = TokenContext.get();
		if(role.getId()==null)
			role.setId(UUID.randomUUID().toString());
		role.setIsDeleted(0);
		role.setCreateUserId(token.getUserId());
		role.setCreateDate(token.getCurrentDate());
		role.setTenantId(token.getTenantId());
		
		return service.insert(role);
	}
	
	/**
	 * 修改角色
	 * @param role
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public Response update(@RequestBody Role role) {
		role.setIsDeleted(0);
		role.setCreateUserId(null);
		role.setCreateDate(null);
		role.setTenantId(TokenContext.get().getTenantId());
		
		return service.update(role);
	}
	
	/**
	 * 删除角色
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public Response delete(HttpServletRequest request) {
		return service.delete(request.getParameter("_id"));
	}
}
