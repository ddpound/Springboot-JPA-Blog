package com.ysj.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysj.blog.model.RoleType;
import com.ysj.blog.model.User;
import com.ysj.blog.repository.UserRepository;

//스프링이 컴포턴트 스캔을 통해서 Bean에 등록 . IOC해준다
// 서비스가 필요한 이유 (1. 트랜잭션 관리 2. 서비스의 의미 때문에)
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	

	@Transactional(readOnly = true)
	public User findUser(String username) {
		User user = userRepository.findByUsername(username).orElseGet(()->{
			return new User();
		});
		return user;
	}
	
	

	@Transactional // 전체가 성공하면 커밋 , 실패하면 롤백
	public void saveUser(User user) {
		String rowPassword = user.getPassword(); // 비밀번호 원문
		String encPassowrd = encoder.encode(rowPassword); // 해쉬코드가됨
		user.setPassword(encPassowrd);
		user.setRole(RoleType.USER);
		userRepository.save(user);

	}
	
	//회원가입 수정 , 트랜잭션이 종료 안되고 난다음에 실행시 변경안됨
	@Transactional
	public void userUpdate(User user) {
		// 수정시에는 JPA 영속성 컨텍스트 User 오브젝트를 영속화 시키고, 영속화된 USER 오브젝트를 수정
		// select 해서 USER 오브젝트를 DB로부터 가져오는 이유는 영속화를 하기위해서
		// 영속화된 오브젝트를 변경하면 자동으로 DB에 UPDATE문을 날려줌
		User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("회원찾기 실패");
		});
		
		// Validate 체크 => oauth이 없으면 수정가능
		if(persistance.getOauth() ==null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
		}
		
		//까먹지 마세요 절대 yml 파일은 업데이트하는게 아닙니다
		

		
		// 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = 커밋 = 더티체킹
		// 영속화된 persistance 객체의 변화가 감지되면 더티체킹해서 알아서 업데이트를 안해도 업뎃을 해준다
		
	}

	

//	@Transactional(readOnly = true) // 전체가 성공하면 커밋 , 실패하면 롤백, select할때 트랜잭션 시작, 서비스종료시에 트랜잭션 종료(정합성)
//	public User loginUser(User user) {
//			return userRepository.findByUsernameAndPassword(user.getUsername() , user.getPassword());
//			
//		} 

}
