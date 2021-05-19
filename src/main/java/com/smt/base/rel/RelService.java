package com.smt.base.rel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;

/**
 * 
 * @author DougLei
 */
@TransactionComponent
public class RelService {
	
	// 获取指定(LeftKey+LeftValue+RightKey)下的RightValue集合
	private Set<String> getRightValues(DataRel rel) {
		List<Object[]> results = SessionContext.getSQLSession().query_("DataRel", "getRightValues", rel);
		if(results.isEmpty())
			return Collections.emptySet();
		
		HashSet<String> rightValues = new HashSet<String>(16);
		results.forEach(result -> rightValues.add(result[0].toString()));
		return rightValues;
	}
	
	/**
	 * 添加数据关联关系
	 * @param list
	 */
	@Transaction
	public void insert(List<DataRel> list) {
		Set<String> existsRightValues = getRightValues(list.get(0));
		if(!existsRightValues.isEmpty()) {
			for(int i=0; i<list.size();i++) {
				if(existsRightValues.contains(list.get(i).getRightValue())) 
					list.remove(i--);
			}
			
			if(list.isEmpty())
				return;
		}
		SessionContext.getTableSession().save(list);
	}

	/**
	 * 删除指定(LeftKey+LeftValue+RightKey)下的所有RightValue
	 * @param rel
	 */
	@Transaction
	public void deleteAll(DataRel rel) {
		if(SessionContext.getSQLSession().query_("DataRel", "getRightValues", rel).isEmpty())
			return;
		
		SessionContext.getSQLSession().executeUpdate("DataRel", "deleteAll", rel);
	}
	
	/**
	 * 删除数据关联关系
	 * @param list
	 */
	@Transaction
	public void delete(List<DataRel> list) {
		Set<String> existsRightValues = getRightValues(list.get(0));
		if(existsRightValues.isEmpty())
			return;
		
		for(int i=0; i<list.size();i++) {
			if(!existsRightValues.contains(list.get(i).getRightValue())) 
				list.remove(i--);
		}
		if(list.isEmpty())
			return;
		
		SessionContext.getSQLSession().executeUpdate("DataRel", "delete", list);
	}
}
