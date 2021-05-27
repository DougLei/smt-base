package com.smt.base.rel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.orm.context.PropagationBehavior;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.douglei.tools.StringUtil;
import com.smt.parent.code.filters.token.TokenContext;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class DataRelService {

	/**
	 * 增删改数据关联关系
	 * @param wrapper
	 */
	@Transaction
	public void operate(DataRelWrapper wrapper) {
		// 先删除旧的关联关系数据
		SessionContext.getSQLSession().executeUpdate("DataRel", "deleteValues", wrapper);
		
		// 再添加新的关联关系数据
		if(StringUtil.isEmpty(wrapper.getChildValues()))
			return;
		
		String[] childValues = wrapper.getChildValues().split(",");
		List<DataRel> list = new ArrayList<DataRel>(childValues.length);
		if(wrapper.getFlag() == 1) {
			for(String childValue: childValues)
				list.add(new DataRel(wrapper.getParentType(), wrapper.getParentValue(), wrapper.getChildType(), childValue, wrapper.getProjectCode(), wrapper.getTenantId()));
		}else {
			for(String childValue: childValues)
				list.add(new DataRel(wrapper.getChildType(), childValue, wrapper.getParentType(), wrapper.getParentValue(), wrapper.getProjectCode(), wrapper.getTenantId()));
		}
		SessionContext.getTableSession().save(list);
	}

	/**
	 * 查询Value集合
	 * @param wrapper
	 * @return 没查到数据时会返回null
	 */
	@Transaction(propagationBehavior=PropagationBehavior.SUPPORTS)
	public List<String> queryValues(DataRelWrapper wrapper) {
		List<Object[]> results = SessionContext.getSQLSession().query_("DataRel", "queryValues", wrapper);
		if(results.isEmpty())
			return null;
		
		List<String> values = new ArrayList<String>(results.size());
		results.forEach(result -> values.add(result[0].toString()));
		return values;
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
}
