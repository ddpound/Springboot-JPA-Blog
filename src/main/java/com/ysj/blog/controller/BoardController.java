package com.ysj.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ysj.blog.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardservie;

	// @AuthenticationPrincipal PrincipalDetail principal 이게 세션접근법
	@GetMapping({ "", "/" })
	public String index(Model model, @PageableDefault(size=3,sort="id",direction=Sort.Direction.DESC) Pageable pageable) { // 컨트롤러에서 세션을 어떻게 찾을까
		model.addAttribute("boards" , boardservie.boardList(pageable));
		
		// /WEB_INF/views/index.jsp
		return "index";  //viewResolver 작동 
	}
	
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model) {
		model.addAttribute("board",boardservie.boardDetailLook(id));
		return "board/detail";
	}
	
	@GetMapping("board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("board",boardservie.boardDetailLook(id));
		return "board/updateForm";
	}
	
	

	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}

}
