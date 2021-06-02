package com.smt.base.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.base.dict.entity.Dict;
import com.smt.base.dict.entity.DictDetail;
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
@RequestMapping("/dict")
public class DictController {
	
	@Autowired
	private DictService service;

	@Autowired
	private QueryExecutor queryExecutor;
	
	/**
	 * 数据字典查询
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/query", method=RequestMethod.GET)
	public Response query(HttpServletRequest request) {
		return queryExecutor.execute("QueryDictList", TokenContext.get(), request, "queryDeleted");
	}
	
	/**
	 * 数据字典明细查询
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/detail/query", method=RequestMethod.GET)
	public Response queryDetail(HttpServletRequest request) {
		return service.queryDetail(request.getParameter("_id"));
	}
	
	// ------------------------------------------------------------------------------------------------------
	/**
	 * 添加数据字典
	 * @param dict
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(Dict dict) {
		TokenEntity token = TokenContext.get();
		if(dict.getId()==null)
			dict.setId(UUID.randomUUID().toString());
		dict.setCreateUserId(token.getUserId());
		dict.setCreateDate(token.getCurrentDate());
		dict.setProjectCode(token.getProjectCode());
		dict.setTenantId(token.getTenantId());
		
		return service.insert(dict);
	}
	
	/**
	 * 修改数据字典
	 * @param dict
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public Response update(Dict dict) {
		dict.setCreateUserId(null);
		dict.setCreateDate(null);
		
		TokenEntity token = TokenContext.get();
		dict.setProjectCode(token.getProjectCode());
		dict.setTenantId(token.getTenantId());
		
		return service.update(dict);
	}
	
	/**
	 * 删除数据字典, 同时会删除明细集合
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public Response delete(HttpServletRequest request) {
		return service.delete(request.getParameter("_id"));
	}
	
	// ------------------------------------------------------------------------------------------------------
	/**
	 * 添加数据字典明细
	 * @param detail
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/detail/insert", method=RequestMethod.POST)
	public Response insertDetail(DictDetail detail) {
		return service.insertDetail(detail);
	}
	
	/**
	 * 修改数据字典明细
	 * @param detail
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/detail/update", method=RequestMethod.POST)
	public Response updateDetail(DictDetail detail) {
		return service.updateDetail(detail);
	}
	
	/**
	 * 删除数据字典明细
	 * @param request
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/detail/delete", method=RequestMethod.DELETE)
	public Response deleteDetail(HttpServletRequest request) {
		List<Integer> ids = new ArrayList<Integer>();
		for(String detailId: request.getParameter("_ids").split(","))
			ids.add(Integer.parseInt(detailId));
		
		service.deleteDetail(ids);
		return new Response(null);
	}
}
