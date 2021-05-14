package com.smt.base.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.parent.code.filters.token.TokenEntity;
import com.smt.parent.code.filters.token.TokenValidateResult;
import com.smt.parent.code.response.Response;
import com.smt.parent.code.spring.web.LoggingResponse;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping("/account")
public class AccountController {

	/**
	 * 登录
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Response validate() {
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
		return new TokenValidateResult(new TokenEntity());
	}
}
