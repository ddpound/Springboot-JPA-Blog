package com.ysj.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
	
	
	
	// @AuthenticationPrincipal PrincipalDetail principal 이게 세션접근법
	@GetMapping({"/",""})
	public String index() { // 컨트롤러에서 세션을 어떻게 찾을까
		// /WEB_INF/views/index.jsp
		return "index";
	}
	
	
	
}
