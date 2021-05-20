package com.smt.base.rel;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.response.Response;
import com.smt.parent.code.spring.web.LoggingResponse;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping("/data/rel")
public class DataRelController {
	
	@Autowired
	private DataRelService service;
	
	/**
	 * 添加数据关联关系
	 * @param builder
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody DataRelBuilder builder) {
		service.insert(builder.build());
		return new Response(builder);
	}
	
	/**
	 * 删除数据关联关系
	 * @param builder
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public Response delete(@RequestBody DataRelBuilder builder, HttpServletRequest request) {
		if("true".equalsIgnoreCase(request.getParameter("deleteAll"))) {
			service.deleteAll(new DataRel(builder.getLeftKey(), builder.getLeftValue(), builder.getRightKey(), 
					null, null, null, builder.getProjectCode(), TokenContext.get().getTenantId()));
		} else{
			service.delete(builder.build());
		}
		return new Response(builder);
	}
}
