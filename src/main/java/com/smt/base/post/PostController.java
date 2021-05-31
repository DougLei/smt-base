package com.smt.base.post;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	private PostService service;
	
	@Autowired
	private QueryExecutor queryExecutor;
	
	/**
	 * 岗位查询
	 * @param request
	 * @return
	 */
	@LoggingResponse(loggingBody=false)
	@RequestMapping(value="/query", method=RequestMethod.GET)
	public Response query(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("queryDeleted", "true".equalsIgnoreCase(request.getParameter("queryDeleted")));
		params.put("token", TokenContext.get());
		
		return queryExecutor.execute("QueryPostList", TokenContext.get(), request, "queryDeleted");
	}
	
	/**
	 * 添加岗位
	 * @param post
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public Response insert(@RequestBody Post post) {
		TokenEntity token = TokenContext.get();
		if(post.getId()==null)
			post.setId(UUID.randomUUID().toString());
		post.setIsDeleted(0);
		post.setCreateUserId(token.getUserId());
		post.setCreateDate(token.getCurrentDate());
		post.setProjectCode(token.getProjectCode());
		post.setTenantId(token.getTenantId());
		
		return service.insert(post);
	}
	
	/**
	 * 修改岗位
	 * @param post
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public Response update(@RequestBody Post post) {
		post.setIsDeleted(0);
		post.setCreateUserId(null);
		post.setCreateDate(null);
		
		TokenEntity token = TokenContext.get();
		post.setProjectCode(token.getProjectCode());
		post.setTenantId(token.getTenantId());
		
		return service.update(post);
	}
	
	/**
	 * 删除岗位
	 * @param postId
	 * @return
	 */
	@LoggingResponse
	@RequestMapping(value="/delete/{postId}", method=RequestMethod.DELETE)
	public Response delete(@PathVariable String postId) {
		return service.delete(postId);
	}
}
