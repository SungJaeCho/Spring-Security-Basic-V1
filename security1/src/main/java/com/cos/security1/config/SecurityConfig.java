package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
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
			;
	}
	

}
