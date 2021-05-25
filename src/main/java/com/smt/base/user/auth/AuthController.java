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
import com.smt.parent.code.filters.FilterEnum;
import com.smt.parent.code.filters.log.LogContext;
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
	private TokenContainer tokenContainer;
	
	@Autowired
	private AuthConfigurationProperties properties;
	
	// 登录前置处理
	private void loginPreprocessing(LoginEntity entity, HttpServletRequest request) {
		if(entity.getProjectCode()==null)
			entity.setProjectCode(request.getParameter("projectcode"));
		if(entity.getTenantId()==null)
			entity.setTenantId(request.getParameter("tenantid"));
		
		entity.setClientIp(HttpUtil.getClientIp(request));
		entity.setTenantId(TempTenantId.VALUE); 
	}
	// 登录
	private Response login_(LoginEntity entity, HttpServletRequest request) {
		if(StringUtil.isEmpty(entity.getLoginName()))
			return new Response(entity, "loginName", "登录名不能为空", "smt.base.login.fail.loginname.null");
		if(StringUtil.isEmpty(entity.getLoginPwd()))
			return new Response(entity, "loginPwd", "登录密码不能为空", "smt.base.login.fail.loginpwd.null");
		if(StringUtil.isEmpty(entity.getTenantId()))
			return new Response(entity, "tenantId", "租户不能为空", "smt.base.login.fail.tenantid.null");
		
		return service.login(entity);
	}
	
	/**
	 * (配置系统)登录
	 * @param entity
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value = {"/clogin", "/vclogin"}, method = RequestMethod.POST)
	public Response clogin(@RequestBody LoginEntity entity, HttpServletRequest request) {
		loginPreprocessing(entity, request);
		
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
		loginPreprocessing(entity, request);
		
		if(StringUtil.isEmpty(entity.getProjectCode()))
			return new Response(entity, "projectCode", "项目编码不能为空", "smt.base.login.fail.projectcode.null");
		return login_(entity, request);
	}
	
	/**
	 * 登出
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value = "/loginout", method = RequestMethod.GET)
	public Response loginout(HttpServletRequest request) {
		TokenEntity entity = TokenContext.get();
		
		if("true".equalsIgnoreCase(request.getParameter("all"))) {
			tokenContainer.removeByUserId(entity.getUserId());
			return new Response(entity.getUserId());
		}
		
		tokenContainer.remove(entity.getValue());
		return new Response(entity.getValue());
	}
	
	// ------------------------------------------------------------------------------------------------------
	// 验证token
	private TokenValidateResult validate_(String token, String clientIp) {
		TokenEntity entity = tokenContainer.get(token);
		if(entity == null)
			return new TokenValidateResult(null, "无效token", "smt.base.token.validate.invalid");
		
		if(properties.isEnableClientIpLimit() && !entity.getClientIp().equals(clientIp)) {
			tokenContainer.remove(token);
			return new TokenValidateResult(entity, "客户端ip变动, 请重新登录", "smt.base.token.validate.ip.changed");
		}
		
		if(entity.getLastOpDate().getTime()+properties.getTokenValidTimes() < entity.getCurrentDate().getTime()) {
			tokenContainer.remove(token);
			return new TokenValidateResult(entity, "token已过期, 请重新登录", "smt.base.token.validate.overdue");
		}
		return new TokenValidateResult(entity);
	}
	
	/**
	 * 验证token
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/token/validate/{token}", method = RequestMethod.GET)
	public TokenValidateResult validate(@PathVariable String token, HttpServletRequest request) {
		TokenValidateResult result = validate_(token, request.getParameter("clientIp"));
		if(result.isSuccess()) {
			TokenEntity entity = result.getEntity();
			
			entity.setLastOpDate(entity.getCurrentDate());
			tokenContainer.update(entity);
		}
		return result;
	}
	
	/**
	 * 修改token数据
	 * @param data
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value = "/token/update", method = RequestMethod.POST)
	public Response updateToken(@RequestBody TokenEntity data, HttpServletRequest request) {
		if(data.getValue() == null)
			data.setValue(request.getHeader(FilterEnum.TOKEN.getHeaderName()));
		if(data.getValue() == null)
			return new Response(null, null, "token不能为空", "smt.base.token.update.fail.value.null");
			
		TokenValidateResult result = validate_(data.getValue(), HttpUtil.getClientIp(request));
		if(result.getEntity() != null) 
			LogContext.loggingUserId(result.getEntity().getUserId());
		
		if(result.isSuccess()) 
			return service.updateToken(result.getEntity(), data);
		return new Response(data.getValue(), null, result.getMessage(), result.getCode());		
	}
}
