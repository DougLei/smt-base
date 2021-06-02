package com.smt.base.rel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.orm.context.PropagationBehavior;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.douglei.tools.StringUtil;
import com.smt.base.SmtBaseException;
import com.smt.parent.code.filters.token.TokenContext;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class DataRelService {
	
	// 添加新的关联关系数据
	private void insert_(DataRelWrapper wrapper) {
		if(StringUtil.isEmpty(wrapper.getChildValues()))
			return;
		
		String projectCode = wrapper.getProjectCode();
		String[] childValues = wrapper.getChildValues().split(",");
		List<DataRel> list = new ArrayList<DataRel>(childValues.length);
		if(wrapper.getFlag() == 1) {
			for(String childValue: childValues)
				list.add(new DataRel(wrapper.getParentType(), wrapper.getParentValue(), wrapper.getChildType(), childValue, projectCode, wrapper.getTenantId()));
		}else {
			for(String childValue: childValues)
				list.add(new DataRel(wrapper.getChildType(), childValue, wrapper.getParentType(), wrapper.getParentValue(), projectCode, wrapper.getTenantId()));
		}
		SessionContext.getTableSession().save(list);
	}
	
	/**
	 * 增删改数据关联关系
	 * @param wrapper
	 */
	@Transaction
	public void operate(DataRelWrapper wrapper) {
		// 先删除旧的关联关系数据; 再添加新的关联关系数据
		SessionContext.getSQLSession().executeUpdate("DataRel", "deleteAll", wrapper);
		insert_(wrapper);
	}

	/**
	 * 增加数据关联关系
	 * @param wrapper
	 */
	@Transaction
	public void insert(DataRelWrapper wrapper) {
		// 先删除旧的关联关系数据; 再添加新的关联关系数据
		SessionContext.getSQLSession().executeUpdate("DataRel", "deleteByChildValues", wrapper);
		insert_(wrapper);
	}
	
	/**
	 * 删除数据关联关系
	 * @param wrapper
	 */
	@Transaction
	public void delete(DataRelWrapper wrapper) {
		SessionContext.getSQLSession().executeUpdate("DataRel", "deleteByChildValues", wrapper);
	}
	
	/**
	 * 删除指定type和value的所有数据
	 * @param type
	 * @param value
	 * @return 
	 */
	@Transaction
	public int deleteAll(Type type, String value) {
		List<Object> params = Arrays.asList(type.name(), value, TokenContext.get().getTenantId());
		return SessionContext.getSqlSession().executeUpdate("delete base_data_rel where left_type=? and left_value=? and tenant_id=?", params) 
				+ SessionContext.getSqlSession().executeUpdate("delete base_data_rel where right_type=? and right_value=? and tenant_id=?", params);
	}
	
	/**
	 * 查询Value集合(只查询linkedProject=true的关联关系, linkedProject=false时会返回null)
	 * @param wrapper
	 * @return 没查到数据时会返回null
	 */
	@Transaction(propagationBehavior=PropagationBehavior.SUPPORTS)
	public List<String> queryValues(DataRelWrapper wrapper) {
		if(wrapper.getProjectCode() == null) 
			throw new SmtBaseException("parent="+wrapper.getParentType()+", child="+wrapper.getChildType()+", 不支持使用queryValues()方法进行查询");
		
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("wrapper", wrapper);
		params.put("parentProjectCodes", wrapper.tokenEntity.getParentProjectCodes());
		
		List<Object[]> results = SessionContext.getSQLSession().query_("DataRel", "queryChildValues", params);
		if(results.isEmpty())
			return null;
		
		List<String> values = new ArrayList<String>(results.size());
		results.forEach(result -> values.add(result[0].toString()));
		return values;
	}
}
