package com.smt.base.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author DougLei
 */
@RestController
@RequestMapping("/link")
public class LinkController {
	
	@Autowired
	private LinkService service;
	
	
	/*
	 * 添加用户和组织机构的关系(支持一个用户多个组织机构的添加, 记得如果存在则报错)
	 * 删除用户和组织机构的关系(记得如果不存在则报错)
	 */
}
