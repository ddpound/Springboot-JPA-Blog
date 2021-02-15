package com.ysj.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.ysj.blog.model.User;


// @Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// 네이밍 쿼리임
	// SELECT * FROM user WHERE username = 1?;
	Optional<User> findByUsername(String username);
	
	
	
	
	//jpa naming 쿼리 전략
	// SELECT * FROM user WHRER username = ? AND password = ? // 이런 쿼리가 동작
//	User findByUsernameAndPassword(String username, String password);
	
// 이것도 가능	
//	@Query(value = "SELECT * FROM user WHRER username = ? AND password = ?", nativeQuery = true)
//	User login(String username, String password);
	
	
	
}
