package com.smt.base.dict;

import java.util.Arrays;
import java.util.List;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.smt.base.SmtBaseException;
import com.smt.base.dict.entity.Dict;
import com.smt.base.dict.entity.DictDetail;
import com.smt.parent.code.response.Response;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class DictService {
	
	// 验证code是否存在
	private boolean codeExists(Dict dict) {
		return SessionContext.getSqlSession().uniqueQuery_(
				"select id from base_dict where code=? and tenant_id=?",
				Arrays.asList(dict.getCode(), dict.getTenantId())) != null;
	}
	// 验证key是否存在
	private boolean keyExists(DictDetail detail) {
		return SessionContext.getSqlSession().uniqueQuery_(
				"select id from base_dict_detail where dict_id=? and key_=?", 
				Arrays.asList(detail.getDictId(), detail.getKey())) != null;
	}
	
	
	/**
	 * 添加数据字典
	 * @param dict
	 * @return
	 */
	@Transaction
	public Response insert(Dict dict) {
		if(codeExists(dict))
			return new Response(dict, "code", "已存在编码为[%s]的数据字典", "smt.base.dict.fail.code.exists", dict.getCode());
		
		// 保存数据字典
		SessionContext.getTableSession().save(dict);
		return new Response(dict);
	}
	
	/**
	 * 修改数据字典
	 * @param dict
	 * @return
	 */
	@Transaction
	public Response update(Dict dict) {
		Dict old = SessionContext.getSqlSession().uniqueQuery(Dict.class, "select * from base_dict where id=?", Arrays.asList(dict.getId()));
		if(old == null)
			throw new SmtBaseException("修改失败, 不存在id为["+dict.getId()+"]的数据字典");
		if(!dict.getCode().equals(old.getCode()) && codeExists(dict))
			return new Response(dict, "code", "已存在编码为[%s]的数据字典", "smt.base.dict.fail.code.exists", dict.getCode());
		
		// 修改数据字典
		SessionContext.getTableSession().update(dict);
		return new Response(dict);
	}
	
	/**
	 * 删除数据字典, 同时会删除明细集合
	 * @param dictId
	 * @return
	 */
	@Transaction
	public Response delete(String dictId) {
		List<Object> list = Arrays.asList(dictId);
		if(SessionContext.getSqlSession().uniqueQuery_("select id from base_dict where id=?", list) == null)
			throw new SmtBaseException("删除失败, 不存在id为["+dictId+"]的数据字典");
		
		SessionContext.getSqlSession().executeUpdate("delete base_dict where id=?", list);
		SessionContext.getSqlSession().executeUpdate("delete base_dict_detail where dict_id=?", list);
		return new Response(dictId);
	}
	
	// ------------------------------------------------------------------------------------------------------
	/**
	 * 添加数据字典明细
	 * @param detail
	 * @return
	 */
	@Transaction
	public Response insertDetail(DictDetail detail) {
		if(SessionContext.getSqlSession().uniqueQuery_("select id from base_dict where id=?", Arrays.asList(detail.getDictId())) == null)
			throw new SmtBaseException("添加数据字典明细失败, 不存在id为["+detail.getDictId()+"]的数据字典");
		
		if(keyExists(detail))
			return new Response(detail, "key", "已存在key为[%s]的数据字典明细", "smt.base.dict.detail.fail.key.exists", detail.getKey());
		
		SessionContext.getTableSession().save(detail);
		return new Response(detail);
	}
	
	/**
	 * 修改数据字典明细
	 * @param detail
	 * @return
	 */
	@Transaction
	public Response updateDetail(DictDetail detail) {
		DictDetail old = SessionContext.getTableSession().uniqueQuery(DictDetail.class, "select * from base_dict_detail where id=?", Arrays.asList(detail.getId()));
		if(old == null)
			throw new SmtBaseException("修改失败, 不存在id为["+detail.getId()+"]的数据字典明细");
		if(detail.getDictId() != old.getDictId())
			throw new SmtBaseException("修改失败, 禁止修改关联的数据字典");
		
		if(!detail.getKey().equals(old.getKey()) && keyExists(detail))
			return new Response(detail, "key", "已存在key为[%s]的数据字典明细", "smt.base.dict.detail.fail.key.exists", detail.getKey());
		
		SessionContext.getTableSession().update(detail);
		return new Response(detail);
	}
	
	/**
	 * 删除数据字典明细
	 * @param detailIds
	 */
	@Transaction
	public void deleteDetail(List<String> detailIds) {
		SessionContext.getSQLSession().executeUpdate("Dict", "deleteDetail", detailIds);
	}
}
