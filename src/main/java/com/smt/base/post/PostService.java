package com.smt.base.post;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.SmtBaseException;
import com.smt.base.rel.DataRelService;
import com.smt.base.rel.Type;
import com.smt.parent.code.response.Response;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class PostService {
	
	@Autowired
	private DataRelService dataRelService;

	// 验证code是否存在
	private boolean codeExists(Post post) {
		return SessionContext.getSqlSession().uniqueQuery_(
				"select id from base_post where code=? and tenant_id=?", 
				Arrays.asList(post.getCode(), post.getTenantId())) != null;
	}
	
	/**
	 * 添加岗位
	 * @param post
	 * @return
	 */
	@Transaction
	public Response insert(Post post) {
		if(codeExists(post))
			return new Response(post, "code", "已存在编码为[%s]的岗位", "smt.base.post.fail.code.exists", post.getCode());
		
		SessionContext.getTableSession().save(post);
		return new Response(post);
	}

	/**
	 * 修改岗位
	 * @param post
	 * @return
	 */
	@Transaction
	public Response update(Post post) {
		Post old = SessionContext.getSqlSession().uniqueQuery(Post.class, "select * from base_post where id=?", Arrays.asList(post.getId()));
		if(old == null || old.getIsDeleted() == 1)
			throw new SmtBaseException("修改失败, 不存在id为["+post.getId()+"]的岗位");
		
		// 判断是否修改了code
		if(!post.getCode().equals(old.getCode()) && codeExists(post))
			return new Response(post, "code", "已存在编码为[%s]的岗位", "smt.base.post.fail.code.exists", post.getCode());
		
		SessionContext.getTableSession().update(post);
		return new Response(post);
	}

	/**
	 * 删除岗位
	 * @param postId
	 * @return
	 */
	@Transaction
	public Response delete(int postId) {
		Post post = SessionContext.getSqlSession().uniqueQuery(Post.class, "select * from base_post where id=?", Arrays.asList(postId));
		if(post == null || post.getIsDeleted() == 1)
			throw new SmtBaseException("删除失败, 不存在id为["+postId+"]的岗位");
		
		SessionContext.getSqlSession().executeUpdate("update base_post set is_deleted=1 where id=?", Arrays.asList(postId));
		dataRelService.deleteAll(Type.POST_CODE, post.getCode());
		return new Response(postId);
	}
}
