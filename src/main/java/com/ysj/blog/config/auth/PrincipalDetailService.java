package com.ysj.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ysj.blog.model.User;
import com.ysj.blog.repository.UserRepository;

@Service
public class PrincipalDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	// 스프링 로그인 요청을 가로챌 때, username, password 변수를 2개를 가로채는데
	// password 부분 처리는 알아서 함
	// username이 DB에 있는지만 확인해주면됨
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User Principal = userRepository.findByUsername(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당사용자를 찾을수 없습니다."+ username);
				});
		
		return new PrincipalDetail(Principal); // 시큐리티의 세션에 유저정보가 저장이 됨. 
	}
	
	
	
	
}
