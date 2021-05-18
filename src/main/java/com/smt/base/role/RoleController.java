package com.smt.base.role;

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
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService service;
	
	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody Role role) {
		TokenEntity token = TokenContext.get();
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
	 * @param roleId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete/{roleId}", method=RequestMethod.DELETE)
	public Response delete(@PathVariable int roleId) {
		return service.delete(roleId);
	}
}
