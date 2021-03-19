package com.ysj.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ysj.blog.model.User;

import lombok.Data;
import lombok.Getter;

// 스프링 시큐리티가 로그인 요청으 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션 저장소에 저장 해준다

@Data
public class PrincipalDetail implements UserDetails {
	private User user; // 이렇게 가져오는건 콤포지션 , 상속방법도 있음

	public PrincipalDetail(User user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정이 만료되지 않았는지 리턴한다 (true 만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true; // false면 만료되서 로그인이 안됨
	}

	// 계정이 잠겨있지 않았는지 리턴한다 (true 만료안됨)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 만료되지 않았는지 리턴한다 (true 만료안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정 활성화 인지 리턴한다. (true 활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}

	// 계정 권한이 갖고있는 권한목록을 리턴한다
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collectors = new ArrayList<>();

//		collectors.add(new GrantedAuthority() {	
//			@Override
//			public String getAuthority() {
//				return "ROLE_"+user.getRole(); // ROLE_USER -> 이렇게 리턴된다
//			}
//		});

		// 위의 자바 5줄짜리랑 같은의미
		// 자바는 add에 함수를 넣을수는 없다 그래서 람다식이필요
		collectors.add(() -> {
			return "ROLE_" + user.getRole();
		});

		return collectors;
	}

}
