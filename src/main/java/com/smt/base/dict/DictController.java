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
	
	
	
	
	
	
}
