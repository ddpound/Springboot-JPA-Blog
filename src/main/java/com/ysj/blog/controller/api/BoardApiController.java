package com.ysj.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysj.blog.config.auth.PrincipalDetail;
import com.ysj.blog.dto.ReplySaveRequestDto;
import com.ysj.blog.dto.ResponseDto;
import com.ysj.blog.model.Board;
import com.ysj.blog.model.Reply;
import com.ysj.blog.model.User;
import com.ysj.blog.service.BoardService;


@RestController
public class BoardApiController {
	
	
	
	
	@Autowired
	private BoardService boardService;
	
	
	@PostMapping("/api/board")
	public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) { 
		boardService.saveBoard(board, principal.getUser());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id){
		boardService.deleteBoard(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id , @RequestBody Board board){
		boardService.updateBoard(id,board);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	// 데이터를 받을 때 컨트롤러에서 dto를 만들어서 받는게 좋다
	// dto 사용하지 않은 이유는, 너무 많은 데이터를 주고받아버리면 엄청난 용량 처리때문에 DTO를 따로 둬서 만든다
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Integer> replysave(@RequestBody ReplySaveRequestDto replySaveRequestDto) { 
		boardService.saveReply(replySaveRequestDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{boardId}/reply/{replyId}")
	public ResponseDto<Integer> replyDelete(@PathVariable int replyId){
		boardService.deleteReply(replyId);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
}
