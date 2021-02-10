package com.ysj.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysj.blog.dto.ResponseDto;
import com.ysj.blog.model.RoleType;
import com.ysj.blog.model.User;
import com.ysj.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	
	
	@PostMapping("/api/user")
	public ResponseDto<Integer> save(@RequestBody User user) {  //username , password, email
		System.out.println("호출완료");
		// 실제 db에 인서트 하고 아래에서 리턴해주기
		user.setRole(RoleType.USER);
		userService.saveUser(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); // 자바 오브젝트로 리턴할때 json으로 리턴해준다
	}
	
	@PostMapping("/api/user/login")
	public ResponseDto<Integer> login(@RequestBody User user, HttpSession session){
		System.out.println("login 호출완료");
		
		User principal = userService.loginUser(user); // 접근 주체 라는 용어(principal)
		
		if(principal != null) {
			session.setAttribute("principal", principal);
		}
		
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
}
