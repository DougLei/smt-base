package com.smt.base.org;

import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/org")
public class OrgController {
	
	@Autowired
	private OrgService service;
	
	@Autowired
	private QueryExecutor queryExecutor;
	
	/**
	 * 组织机构查询
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/query", method=RequestMethod.GET)
	public Response query(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("queryDeleted", "true".equalsIgnoreCase(request.getParameter("queryDeleted")));
		params.put("tenantId", TokenContext.get().getTenantId());
		
		return queryExecutor.execute("QueryOrgList", params, request, "queryDeleted");
	}
	
	/**
	 * 根据组织机构code, 查询用户集合
	 * @param name
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/users/query", method=RequestMethod.GET)
	public Response queryUsers(HttpServletRequest request) {
		// 获取所有相关的组织机构code集合, 并构建sql查询参数实例
		String code = request.getParameter("CODE");
		List<String> codes =service.getChildCodes(code);
		codes.add(code);
		
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("codes", codes);
		params.put("tenantId", TokenContext.get().getTenantId());
		
		// 执行查询
		return queryExecutor.execute("QueryOrgUserList", params, request, "CODE");
	} 
	
	/**
	 * 添加组织机构
	 * @param org
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody Org org) {
		TokenEntity token = TokenContext.get();
		if(org.getId()==null)
			org.setId(UUID.randomUUID().toString());
		org.setIsDeleted(0);
		org.setCreateUserId(token.getUserId());
		org.setCreateDate(token.getCurrentDate());
		org.setTenantId(token.getTenantId());
		
		return service.insert(org);
	}
	
	/**
	 * 修改组织机构
	 * @param org
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public Response update(@RequestBody Org org) {
		org.setIsDeleted(0);
		org.setCreateUserId(null);
		org.setCreateDate(null);
		org.setTenantId(TokenContext.get().getTenantId());
		
		return service.update(org);
	}
	
	/**
	 * 删除组织机构
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public Response delete(HttpServletRequest request) {
		return service.delete(request.getParameter("_id"));
	}
}
