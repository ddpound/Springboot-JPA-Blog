package com.ysj.blog.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=Exception.class)
	public String handleArgumentException(Exception e) {
		return "<h1>"+e.getMessage()+"</h1>";
	}

}
