package com.ysj.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller // 스프링이 com.ysj.blog 패키지 이하를 스캔해서 특정 어노테이션이 붙어있는 클래스파일들을 new(IOC) 해서
                     // 스프링 컨테이너에 관리해줍니다.

@RestController
public class BlogControllerTest {
	
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello</h1>";
	}
	

}
