package com.smt.base.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.query.QueryExecutor;
import com.smt.parent.code.response.Response;
import com.smt.parent.code.spring.web.LoggingResponse;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private QueryExecutor queryExecutor;
	
	/**
	 * 用户查询
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/query", method=RequestMethod.GET)
	public Response query(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("queryDeleted", "true".equalsIgnoreCase(request.getParameter("queryDeleted")));
		params.put("tenantId", TokenContext.get().getTenantId());
		
		return queryExecutor.execute("QueryUserList", params, request);
	}
	
	/**
	 * 用户查询(流程服务)
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/query4JBPM", method=RequestMethod.GET)
	public Response query4JBPM(HttpServletRequest request) {
		return queryExecutor.execute("QueryUserList4JBPM", TokenContext.get().getTenantId(), request);
	}
	
	/**
	 * 查询账户
	 * @param entity
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/account/query", method=RequestMethod.POST)
	public Response queryAccount(@RequestBody AccountBuilder builder) {
		return service.queryAccount(builder);
	}
	
	// ------------------------------------------------------------------------------------------------------
	/**
	 * 添加用户
	 * @param builder
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody UserBuilder builder, HttpServletRequest request) {
		return service.insert(builder, !"false".equalsIgnoreCase(request.getParameter("openAccount")));
	}
	
	/**
	 * 修改用户
	 * @param builder
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public Response update(@RequestBody UserBuilder builder) {
		return service.update(builder);
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public Response delete(HttpServletRequest request) {
		return service.delete(request.getParameter("_id"));
	}
	
	// ------------------------------------------------------------------------------------------------------
	/**
	 * 开通账户
	 * @param builder
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/account/open", method=RequestMethod.POST)
	public Response openAccount(@RequestBody AccountBuilder builder) {
		return service.openAccount(builder);
	}
	
	/**
	 * 启用账户
	 * @param builder
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/account/enable", method=RequestMethod.POST)
	public Response enableAccount(@RequestBody AccountBuilder builder) {
		return service.enableAccount(builder);
	}
	
	/**
	 * 禁用账户
	 * @param builder
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/account/disable", method=RequestMethod.POST)
	public Response disableAccount(@RequestBody AccountBuilder builder) {
		return service.disableAccount(builder);
	}
	
	/**
	 * 删除账户
	 * @param accountId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/account/delete/{accountId}", method=RequestMethod.DELETE)
	public Response deleteAccount(@PathVariable int accountId) {
		return service.deleteAccount(accountId);
	}
	
	/**
	 * 修改账户的登录名和登录密码
	 * @param builder
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/account/name_pwd/update", method=RequestMethod.POST)
	public Response updateLoginNameAndLoginPwd(@RequestBody AccountBuilder builder) {
		return service.updateLoginNameAndLoginPwd(builder);
	}
}
