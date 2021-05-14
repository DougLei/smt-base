package com.smt.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.douglei.orm.spring.boot.starter.TransactionComponentScan;

/**
 * 
 * @author DougLei
 */
@SpringBootApplication
@ComponentScan("com.smt")
@TransactionComponentScan(packages = "com.smt")
public class SmtBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmtBaseApplication.class, args);
	}
}
