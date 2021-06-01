package com.smt.base.rel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	 * 增删改数据关联关系
	 * @param wrapper
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/operate", method=RequestMethod.POST)
	public Response operate(@RequestBody DataRelWrapper wrapper) {
		service.operate(wrapper);
		return new Response(wrapper);
	}
	
	/**
	 * 查询Value集合
	 * @param wrapper
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/value/query", method=RequestMethod.POST)
	public Response queryValues(@RequestBody DataRelWrapper wrapper) {
		return new Response(service.queryValues(wrapper));
	}
	
	/**
	 * 增加数据关联关系
	 * @param wrapper
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody DataRelWrapper wrapper) {
		service.insert(wrapper);
		return new Response(wrapper);
	}
	
	/**
	 * 删除数据关联关系
	 * @param wrapper
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public Response delete(@RequestBody DataRelWrapper wrapper) {
		service.delete(wrapper);
		return new Response(wrapper);
	}
}
