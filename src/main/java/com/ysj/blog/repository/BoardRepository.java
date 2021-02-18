package com.ysj.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ysj.blog.model.Board;



public interface BoardRepository extends JpaRepository<Board, Integer>{
	
	
}
