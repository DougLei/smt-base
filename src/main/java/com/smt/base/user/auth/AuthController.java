package com.smt.base.user.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.douglei.tools.StringUtil;
import com.douglei.tools.web.HttpUtil;
import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;
import com.smt.parent.code.filters.token.TokenValidateResult;
import com.smt.parent.code.response.Response;
import com.smt.parent.code.spring.web.LoggingResponse;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping
public class AuthController {
		
	@Autowired
	private AuthService service;
	
	@Autowired
	private AuthConfigurationProperties properties;
	
	/**
	 * (配置系统)登录
	 * @param entity
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value = "/clogin", method = RequestMethod.POST)
	public Response clogin(@RequestBody LoginEntity entity, HttpServletRequest request) {
		entity.setClientIp(HttpUtil.getClientIp(request));
		
		if(properties.getCloginAccounts() == null)
			return new Response(entity, null, "无权登录配置系统, 请联系管理员", "smt.base.clogin.fail.no.right");
		
		for(CLoginAccount account: properties.getCloginAccounts()) {
			if(account.getLoginName().equals(entity.getLoginName()) && account.getTenantId().equals(entity.getTenantId())) {
				entity.setProjectCode(null); // 置空项目code
				return login_(entity, request);
			}
		}
		return new Response(entity, null, "无权登录配置系统, 请联系管理员", "smt.base.clogin.fail.no.right");
	}
	
	/**
	 * 登录
	 * @param entity
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Response login(@RequestBody LoginEntity entity, HttpServletRequest request) {
		entity.setClientIp(HttpUtil.getClientIp(request));
		
		if(StringUtil.isEmpty(entity.getProjectCode()))
			return new Response(entity, "projectCode", "项目信息不能为空", "smt.base.login.fail.projectcode.null");
		return login_(entity, request);
	}
	
	// 登录
	private Response login_(LoginEntity entity, HttpServletRequest request) {
		if(StringUtil.isEmpty(entity.getLoginName()))
			return new Response(entity, "loginName", "登录名不能为空", "smt.base.login.fail.loginname.null");
		if(StringUtil.isEmpty(entity.getLoginPwd()))
			return new Response(entity, "loginPwd", "登录密码不能为空", "smt.base.login.fail.loginpwd.null");
		if(StringUtil.isEmpty(entity.getTenantId()))
			return new Response(entity, "tenantId", "租户信息不能为空", "smt.base.login.fail.tenantid.null");
		
		return service.login(entity);
	}
	
	/**
	 * 登出
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value = "/loginout", method = RequestMethod.GET)
	public Response loginout() {
		String token = TokenContext.get().getValue();
		TokenContainer.remove(token);
		return new Response(token);
	}
	
	/**
	 * 验证token
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/token/validate/{token}", method = RequestMethod.GET)
	public TokenValidateResult validate(@PathVariable String token, HttpServletRequest request) {
		TokenEntity entity = TokenContainer.get(token);
		if(entity == null)
			return new TokenValidateResult("无效token", "smt.base.token.validate.invalid");
		
		if(properties.isEnableIpLimit() && !entity.getClientIp().equals(request.getParameter("clientIp"))) {
			TokenContainer.remove(token);
			return new TokenValidateResult("客户端ip变动, 请重新登录", "smt.base.token.validate.ip.changed");
		}
		
		if(entity.getLastOpDate().getTime()+properties.getTokenValidTimes() < entity.getCurrentDate().getTime()) {
			TokenContainer.remove(token);
			return new TokenValidateResult("token已过期, 请重新登录", "smt.base.token.validate.overdue");
		}
		
		entity.setLastOpDate(entity.getCurrentDate());
		TokenContainer.update(entity);
		return new TokenValidateResult(entity);
	}
}
