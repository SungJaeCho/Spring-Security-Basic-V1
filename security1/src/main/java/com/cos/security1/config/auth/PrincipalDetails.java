package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

// 시큐리티가 /login 주소요청이 오면 낚아채서 로그인을 진행시킴
// 로그인진행 완료되면 시큐리티 session을 만들어줌 (Security ContextHolder)
// 오브젝트타입 => Authentication 타입 객체
// Authentication 안에 User정보가 있어야 됨.
// User오브젝트타입 => UserDetails타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private User user; //콤포지션
	private Map<String, Object> attributes;
	
	// 일반로그인 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	// OAuth2 로그인 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	// 해당유저의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 우리 사이트! 1년동안 회원이 로그인을 안하면 휴면계정으로 하기로 했다면 여기서 체크가능
		// 현재시간 - 로긴시간 => 1년초과시 return false
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}
