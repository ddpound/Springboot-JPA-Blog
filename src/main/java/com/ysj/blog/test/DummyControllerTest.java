package com.ysj.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysj.blog.model.RoleType;
import com.ysj.blog.model.User;
import com.ysj.blog.repository.UserRepository;

//data를 리턴해주는 레트스 컨트롤러
@RestController
public class DummyControllerTest {
	
	@Autowired // 원래는 널이지만 메모리에 알아서 넣어주는 중요한 어노테이션 , 의존성 주입
	private UserRepository userRepository;
	
	@DeleteMapping("dummy/user/{id}")
	public String Delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제 실패하였습니다";
		}
		
		return "삭제되었습니다"+id;
	}
	
	
	
	@Transactional
	@PutMapping("dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		System.out.println(id);
		System.out.println(requestUser.getPassword());
		System.out.println(requestUser.getEmail());	
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException();
		});
		
		
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		//더티체킹을 이용한 업데이트
		
		return user;
	}
	
	
	
	
	@GetMapping("dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	@GetMapping("dummy/user")
	public List<User> pageList(@PageableDefault(size = 3 , sort = "id" , direction = Sort.Direction.DESC)Pageable pageable){
		
		 Page<User> pagingUser =    userRepository.findAll(pageable);
		 
		 List<User> users = pagingUser.getContent();
		 
		 return users;
	}
	
	
	
	
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
//			@Override
//			public User get() {
//				// TODO Auto-generated method stub
//				return new User();
//			}
//		});
		
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없습니다  "+ id);
			}
			
		});
		
		
		// user 객체는 자바 오브젝트임 근데 요청은 웹브라우저임
		// 메세지 컨버터가 작동해서 자바의 오브젝트를 웹페이지에 리턴시 유저 오브젝트를 json으로 변환해서 던져줌
		return user;
	}
	
	
	
	@PostMapping("/dummy/join")
	public String join(User user) {
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getEmail());
		
		
		
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입 완료";
	}
	
}
