package com.smt.base;

import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionComponent;

@TransactionComponent
public class Test {
	
	@Transaction
	public void test() {
		
	}
}
