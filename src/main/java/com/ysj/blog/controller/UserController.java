package com.ysj.blog.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


//인증이 안된 사용자들이 출입할수 있는 경로를 auth라고 된 경로만 혀용 
// /auth/** 
// 그냥 주소가 / 이면 index.jsp 허용
//static 이하에 있는  /js/* , /css/** , /image/** 는 허용

@Controller
public class UserController {
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		
		return "user/loginForm";
	}
	
	
	@GetMapping("/auth/kakao/callback")
	public @ResponseBody String kakaoCallback(String code) {  // Data를 리턴해주는 컨트롤러 함수, 코드값을 리퀘스트바디가 담아주나봄
		
		// okhttp 각각의 라이브러리가 잇따
		// Retrofit2  -> 이건 안드로이드에서쓴느 라이브러리
		// HttpURLConnection url = new HttpsURLConnection(){} -> 옛날에는 이런식으로 썼지만 너무 불편했다
		
		
		// Post 방식으로 key=value 데이터를 요청(카카오 쪽으로 )
		//Http Header 오브젝트 생성
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 컨텐트 타입을 헤더에 받는다
		
		//여기에 post 바디 값을 담아낸다
		// http body 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type" , "authorization_code");
		params.add("client_id" , "d87f150ab002b12064aef198ee264bdd");
		params.add("redirect_uri" , "http://localhost:8080/auth/kakao/callback");
		params.add("code" , code);
		// 주의점 원래 위의 코드들을 직접 날로 입력하는건 좋지않다 , 다음에는 변수로 만들어서 넣는게 좋음
		
		// kakaoTokenRequest 이녀석이 바디데이터와 헤더값을 가지고있는 엔티티가 된다
		// 즉 http header와 http body를 하나의 오브젝트에 담아낸다
		// 왜 이렇게 담아내냐면 아래의 rt.exchage의 exchange가 HttpEntity형식으로만 받아내기 때문이다
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, headers);
		
		// 실제로 요청부분
		// http요청하기 - Post 방식으로 - 그리고 responese 변수의 응답 받음.
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST, // 포스트로 요청한다고 알리고
				kakaoTokenRequest, // 3번째로 넣을께 헤더와 데이터값
				String.class // 응답을 받을 타입을 정해줌 (스트링타입의 클래스)
				
				);
		
		
		return "카카오 토큰 요청 완료 : 토큰 요청에 대한 응답: "+response;
	}
	
	@GetMapping("/user/updateForm")
	public String udateForm() {
		
		return "user/updateForm";
	}
	
	
	
	
}
