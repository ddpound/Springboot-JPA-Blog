package com.ysj.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysj.blog.model.User;
import com.ysj.blog.repository.UserRepository;


//스프링이 컴포턴트 스캔을 통해서 Bean에 등록 . IOC해준다
// 서비스가 필요한 이유 (1. 트랜잭션 관리 2. 서비스의 의미 때문에)
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional// 전체가 성공하면 커밋 , 실패하면 롤백
	public void saveUser(User user) {
			userRepository.save(user);
			
		} 
	
	@Transactional(readOnly = true) // 전체가 성공하면 커밋 , 실패하면 롤백, select할때 트랜잭션 시작, 서비스종료시에 트랜잭션 종료(정합성)
	public User loginUser(User user) {
			return userRepository.findByUsernameAndPassword(user.getUsername() , user.getPassword());
			
		} 
	
	

}
