package com.ysj.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysj.blog.config.auth.PrincipalDetail;
import com.ysj.blog.dto.ResponseDto;
import com.ysj.blog.model.RoleType;
import com.ysj.blog.model.User;
import com.ysj.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	


// 밑의 전통적이 방식의 로그인을 위한 멤버변수호출
//	@Autowired
//	private HttpSession session;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) {  //username , password, email
		System.out.println("호출완료");
		// 실제 db에 인서트 하고 아래에서 리턴해주기
		userService.saveUser(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); // 자바 오브젝트로 리턴할때 json으로 리턴해준다
	}
	
	// 이런식으로 안하고 스프링 시큐리티를 이용해서 로그인을 함
//	@PostMapping("/api/user/login")
//	public ResponseDto<Integer> login(@RequestBody User user){
//		System.out.println("login 호출완료");
//		
//		User principal = userService.loginUser(user); // 접근 주체 라는 용어(principal)
//		
//		if(principal != null) {
//			session.setAttribute("principal", principal);
//		}
//		
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	// key = value, x-www-form-urlencoded 로 받고싶으면 @RequestBody 를 안해도 되고 USER객체만 받아도 스프링에서 알아서 해준다
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user, @AuthenticationPrincipal PrincipalDetail principal
			, HttpSession session){  // json데이터로 받고싶으면 리퀘스트바디가 필요
		userService.userUpdate(user);
		// 여기서는 트랜잭션이 종료되기 때문에 DB값은 변경이 됐음
		
		
		// 하지만 세션값은 변경되지 않은 상태이기 때문에 우리가 직접 세션값을 변경해줄 것임
		Authentication authentiction =
				new UsernamePasswordAuthenticationToken(principal, null , principal.getAuthorities());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentiction); // 강제로 세션값 변경
		session.setAttribute("SPRING_SECURITY_CONTEXT",securityContext);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
	}
	
	
	
}
