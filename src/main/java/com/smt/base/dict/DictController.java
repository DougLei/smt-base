package com.smt.base.dict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smt.base.dict.entity.Dict;
import com.smt.base.dict.entity.DictDetail;
import com.smt.parent.code.filters.token.TokenContext;
import com.smt.parent.code.filters.token.TokenEntity;
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
	
	/**
	 * 添加数据字典
	 * @param dict
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(Dict dict) {
		TokenEntity token = TokenContext.get();
		dict.setCreateUserId(token.getUserId());
		dict.setCreateDate(token.getCurrentDate());
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
		dict.setTenantId(TokenContext.get().getTenantId());
		
		return service.update(dict);
	}
	
	/**
	 * 删除数据字典, 同时会删除明细集合
	 * @param dictId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete/{dictId}", method=RequestMethod.DELETE)
	public Response delete(@PathVariable int dictId) {
		return service.delete(dictId);
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
	 * @param detailIds
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/detail/delete/{detailIds}", method=RequestMethod.DELETE)
	public Response deleteDetail(@PathVariable String detailIds) {
		List<Integer> ids = new ArrayList<Integer>();
		for(String detailId: detailIds.split(","))
			ids.add(Integer.parseInt(detailId));
		
		service.deleteDetail(ids);
		return new Response(detailIds);
	}
}
