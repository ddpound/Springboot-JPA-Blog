package com.ysj.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysj.blog.model.User;


// @Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	//jpa naming 전략
	
	
	
}
