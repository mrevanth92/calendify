package com.postman.calendify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postman.calendify.models.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{
	public User findByUsername(String username); 
}
