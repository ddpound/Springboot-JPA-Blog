package com.ysj.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ysj.blog.config.auth.PrincipalDetailService;

//아래 세개는 세트임 필수

// 빈등록이라는 의미 : spring 컨테이너에서 객체를 관리할수 있게 하는것
@Configuration // 이러면 빈 등록완료
@EnableWebSecurity // 필터를 거는것, //필터추가 // 모든 주소값을 가로채서 받는다
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean // (IOC가 됨, 스프링이 리턴값을 관리) // 비밀번호를 해쉬로 인코더하는거임
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
		
	}
	
	
	// 시큐리티가 대신 로그인을 해주는데 password를 가로채기를 하는데
	// 해당 password가 뭘로 해쉬가 되서 회원가입이 되었는지 알아야 
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD()); //패스워드 인코더 저거 위에거라고 알려줘야함
	}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() //csrf 토큰 비활성화 (테스트시 잠시 걸어두는게 좋음)
			.authorizeRequests()
				.antMatchers("/" , "/auth/**", "/js/**" , "/css/**" , "/image/**", "/dummy/**") // auth로 들어오는 모든 값들은
				.permitAll() //permitAll 모두 허용 , 인증 안된이들에게도
				.anyRequest() // 그외 나머지는 
				.authenticated() // 모두 제한
			.and()
				.formLogin()
				.loginPage("/auth/loginForm") // 우리가 만든 로그인 폼을 보여준다
				.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당주소로 오는 요청을 가로챈다. 대신로그인해줌
				.defaultSuccessUrl("/"); //정상적으로 요청이 완료가되면
				
		
	}
	

}
