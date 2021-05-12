package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// CRUD 함수
// @Repository라는 어노테이션이 없어도 IOC가 됨 이유는 JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer>{

}
