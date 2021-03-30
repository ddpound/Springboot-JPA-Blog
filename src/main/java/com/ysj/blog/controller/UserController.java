package com.ysj.blog.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysj.blog.model.KakaoProfile;
import com.ysj.blog.model.OAuthToken;
import com.ysj.blog.model.User;
import com.ysj.blog.service.UserService;




//인증이 안된 사용자들이 출입할수 있는 경로를 auth라고 된 경로만 혀용 
// /auth/** 
// 그냥 주소가 / 이면 index.jsp 허용
//static 이하에 있는  /js/* , /css/** , /image/** 는 허용

@Controller
public class UserController {
	
	@Value("${ysj.key}")
	private String ysjKey;
	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		
		return "user/loginForm";
	}
	
	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) {  // Data를 리턴해주는 컨트롤러 함수, 코드값을 리퀘스트바디가 담아주나봄
		
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
		
		//Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		//파싱할때 이름이 틀리거나 오류날수있으니 트라이캣치문으로 한다
		try {
			oauthToken = objectMapper.readValue( response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		// 여기에있는 토큰이 즉 100줄에 있는 토큰에 들어간다는 의미
		//System.out.println("카카오 엑세스 토큰: "+oauthToken.getAccess_token());
		
		// Post 방식으로 key=value 데이터를 요청(카카오 쪽으로 )
		//Http Header 오브젝트 생성
		RestTemplate rt2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 컨텐트 타입을 헤더에 받는다
		
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(headers2);
		
		// 실제로 요청부분
		// http요청하기 - Post 방식으로 - 그리고 responese 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST, // 포스트로 요청한다고 알리고
				kakaoProfileRequest2, // 3번째로 넣을께 헤더와 데이터값
				String.class // 응답을 받을 타입을 정해줌 (스트링타입의 클래스)
				);
		
		// json 으로 변환된 겟바디
		//System.out.println(response2.getBody());
		
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		//파싱할때 이름이 틀리거나 오류날수있으니 트라이캣치문으로 한다
		try {
			kakaoProfile = objectMapper2.readValue( response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		// User오브젝트  :  username, password, eamil
		System.out.println("카카오 ID 번호: "+kakaoProfile.getId());
		System.out.println("카카오 이메일: "+kakaoProfile.getKakao_account().getEmail());
		
		System.out.println("블로그서버 유저네임: "+ kakaoProfile.getKakao_account().getEmail()+"_"
		+kakaoProfile.getId());
		System.out.println("블로그 서버 이메일: "+kakaoProfile.getKakao_account().getEmail());
		UUID garbagePassword = UUID.randomUUID();
		System.out.println("블로그 패스워드: "+ ysjKey);
		//UUID란 - > 중복되지않는 어떤 특정값을 만들어내는 알고리즘
		
		User kakaoUser= User.builder()
				.username(kakaoProfile.getKakao_account().getEmail()+"_"
						+kakaoProfile.getId())
				.password(ysjKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();
		
		// 가입자인지 비가입자인지 체크
		// 기존 가입자가 있으면 회원가입 안하고 없으면 회원가입처리하고
		User originUser = userService.findUser(kakaoUser.getUsername());
		if(originUser.getUsername()==null) {
			System.out.println("기존회원이 아닙입니다");
			userService.saveUser(kakaoUser);
		}
		System.out.println("기존회원입니다");
		
		// 즉 계속 변경되는 UUID 덕에 어쓰케이션이 베드값을 내준것이다
		// 저기 ysjKey가 노출된다면 모든 OAuth 가 다 뚫린다고 생각하면 된다
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), ysjKey));
		SecurityContextHolder.getContext().setAuthentication(authentication); // 세션등록덕에 접근필요가 없음
		
		
		return "redirect:/";
	}
	
	@GetMapping("/user/updateForm")
	public String udateForm() {
		
		return "user/updateForm";
	}
	
	
	
	
}
