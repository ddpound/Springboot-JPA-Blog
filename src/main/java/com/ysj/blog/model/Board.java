package com.ysj.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false , length = 100)
	private String title;
	
	@Lob
	private String content;
	
	private int count;
	
	// 한명의 유저가 여러개의 글을 작성할수 있다
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE) // 연관관계의 주인이 아니다 난  fk키가 아니니깐 db에 컬럼 만들지 마라
	@JsonIgnoreProperties({"board"}) // 무한,보안참조를 방지하기 위함 리스트 내부의 똑같은 보드 참조는 하지 않게 , 무시하게 만들어준다
	@OrderBy("id desc")
	private List<Reply> replys;
	
	
	@CreationTimestamp
	private Timestamp createDate;
	
	
	
}
