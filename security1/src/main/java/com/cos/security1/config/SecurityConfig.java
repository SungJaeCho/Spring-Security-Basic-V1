package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

// 1.코드받기(인증) 2.엑세스토큰(권한) 3.사용자프로필 정보 가져옴 4-1.그정보를 토대로 회원가입을 자동으로 진행
// 4-2(이메일,전화번호,이름,아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (Vip등급)

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
//@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
//	private final PrincipalOauth2UserService principalOauth2UserService;
	private PrincipalOauth2UserService principalOauth2UserService;
	
	// 해당 메서드의 리턴되는 오브젝트를 Ioc로 등록해줌
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder(); 
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()  // /user는 로그인한사람만 들어올수있고
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // /manager는 권한이 ROLE_ADMIN or ROLE_MANAGER만
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll() // 이외에는 권한 허용
			.and()
			.formLogin()
			.loginPage("/loginForm") //로그인되지 않으면 로그인페이지로 이동
			.loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌. 컨트롤러에서 /login안만들어도됨
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/loginForm") // 구글 로그인이 완료된 뒤의 후처리가 필요함. Tip. 코드X, (엑세스토큰+사용자프로필정보 O)
			.userInfoEndpoint()
			.userService(principalOauth2UserService)
			;
	}
	

}
