package com.smt.base.rel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.douglei.orm.context.PropagationBehavior;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;
import com.douglei.tools.StringUtil;

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
				list.add(new DataRel(wrapper.getParentKey(), wrapper.getParentValue(), wrapper.getChildKey(), childValue, wrapper.getProjectCode(), wrapper.getTenantId()));
		}else {
			for(String childValue: childValues)
				list.add(new DataRel(wrapper.getChildKey(), childValue, wrapper.getParentKey(), wrapper.getParentValue(), wrapper.getProjectCode(), wrapper.getTenantId()));
		}
		SessionContext.getTableSession().save(list);
	}

	/**
	 * 查询Value集合
	 * @param wrapper
	 * @return
	 */
	@Transaction(propagationBehavior=PropagationBehavior.SUPPORTS)
	public List<String> queryValues(DataRelWrapper wrapper) {
		List<Object[]> results = SessionContext.getSQLSession().query_("DataRel", "queryValues", wrapper);
		if(results.isEmpty())
			return Collections.emptyList();
		
		List<String> values = new ArrayList<String>(results.size());
		results.forEach(result -> values.add(result[0].toString()));
		return values;
	}
}
