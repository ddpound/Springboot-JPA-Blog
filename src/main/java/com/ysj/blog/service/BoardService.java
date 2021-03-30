package com.ysj.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysj.blog.dto.ReplySaveRequestDto;
import com.ysj.blog.model.Board;
import com.ysj.blog.model.Reply;
import com.ysj.blog.model.RoleType;
import com.ysj.blog.model.User;
import com.ysj.blog.repository.BoardRepository;
import com.ysj.blog.repository.ReplyRepository;
import com.ysj.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//스프링이 컴포턴트 스캔을 통해서 Bean에 등록 . IOC해준다
// 서비스가 필요한 이유 (1. 트랜잭션 관리 2. 서비스의 의미 때문에)
@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final ReplyRepository replyRepository;
	
	@Transactional // 전체가 성공하면 커밋 , 실패하면 롤백
	public void saveBoard(Board board, User user) { // title, content
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);

	}

	@Transactional(readOnly = true)
	public Page<Board> boardList(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Board boardDetailLook(int id) {
		return boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을수 없음:");
		});

	}

	@Transactional
	public void deleteBoard(int id) {
		boardRepository.deleteById(id);

	}

	@Transactional
	public void updateBoard(int id, Board requestBoard) {
		Board board = boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을수 없음:");
		}); // 영속화시키는것
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		//해당 함수가 종료시 트랜잭션이 service가 종료될때 트랜잭션이 종료됩니다. 이때 더티체킹이 일어남 - 자동업데이트 됨 db쪽으로
		//flush
	}
	
	@Transactional
	public void saveReply(ReplySaveRequestDto replySaveRequestDto) {
//		User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(() -> {
//			return new IllegalArgumentException("댓글쓰기 실패 : 유저 아이디를 찾을수 없음:");
//		}); // 영속화시키는것
//		
//		Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(() -> {
//			return new IllegalArgumentException("댓글쓰기 실패 : 게시글 아이디를 찾을수 없음:");
//		}); // 영속화시키는것
//		
//		Reply reply = Reply.builder()
//				.user(user)
//				.board(board)
//				.content(replySaveRequestDto.getContent())
//				.build();
		
//		replySaveRequestDto.setUser(user);
//		replySaveRequestDto.setBoard(board);
		//오브젝트를 넣어준것
		int result = replyRepository.mSave(replySaveRequestDto.getUserId(),replySaveRequestDto.getBoardId(),replySaveRequestDto.getContent());
		System.out.println(result); // 오브젝트를 통째로 시스아웃 할시 toString()이 알아서 발동된다
		
		// 순서는 알아서 잘들어감
		
	}
	
	@Transactional
	public void deleteReply(int replyId) {
		replyRepository.deleteById(replyId);
		
	}

}
