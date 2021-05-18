package com.smt.base.dict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping("/dict")
public class DictController {
	
	@Autowired
	private DictService service;
	
	/*
	 * 添加字典数据(支持同时添加明细集合)
	 * 修改字典数据(支持同时修改明细集合)
	 * 删除字典数据(支持同时删除明细集合)
	 * 
	 * 添加明细
	 * 修改明细
	 * 删除明细
	 */
	
	
	
	
}
