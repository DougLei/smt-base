package com.smt.base.org;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;
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
	
	/**
	 * 添加组织机构
	 * @param org
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody Org org) {
		TokenEntity token = TokenContext.get();
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
	 * @param orgId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete/{orgId}", method=RequestMethod.DELETE)
	public Response delete(@PathVariable int orgId) {
		return service.delete(orgId);
	}
}
