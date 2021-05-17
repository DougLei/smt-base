package com.smt.base.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.base.user.entity.AccountBuilder;
import com.smt.base.user.entity.UserBuilder;
import com.smt.parent.code.filters.token.TokenEntity;
import com.smt.parent.code.filters.token.TokenValidateResult;
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
	
	/**
	 * 登录
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Response login() {
		// TODO 临时返回空对象
		Response response = new Response(null);
		return response;
	}

	/**
	 * 验证token
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/token/validate/{token}", method = RequestMethod.GET)
	public TokenValidateResult validate(@PathVariable String token) {
		// TODO 临时验证空对象
		TokenEntity entity = new TokenEntity();
		entity.setAccountId("accountId111111111111111111111111111");
		entity.setUserId("userId111111111111111111111111111111");
		entity.setProjectCode("projectCode");
		entity.setTenantId("tenantId1111111111111111111111111111");
		return new TokenValidateResult(entity);
	}
	
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
	 * @param userId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete/{userId}", method=RequestMethod.DELETE)
	public Response delete(@PathVariable String userId) {
		return service.delete(userId);
	}
	
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
