package com.smt.base.link;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.base.link.entity.user_org.UserOrgLinkBuilder;
import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.response.Response;
import com.smt.parent.code.spring.web.LoggingResponse;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping("/link")
public class LinkController {
	
	@Autowired
	private LinkService service;
	
	/**
	 * 添加用户和组织机构的关系
	 * @param builder
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/user/org/link/insert", method=RequestMethod.POST)
	public Response insert4UserAndOrgLink(@RequestBody UserOrgLinkBuilder builder) {
		service.insert4UserAndOrgLink(builder.build());
		return new Response(builder);
	}
	
	/**
	 * 删除用户和组织机构的关系
	 * @param builder
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/user/org/link/delete", method=RequestMethod.POST)
	public Response delete4UserAndOrgLink(@RequestBody UserOrgLinkBuilder builder, HttpServletRequest request) {
		if("true".equalsIgnoreCase(request.getParameter("clear")))
			service.clear4UserAndOrgLink(builder.getOrgCode(), TokenContext.get().getTenantId());
		else
			service.delete4UserAndOrgLink(builder.build());
		return new Response(builder);
	}
}
