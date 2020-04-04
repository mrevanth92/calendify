package com.postman.calendify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postman.calendify.models.User;
import com.postman.calendify.repository.IUserRepository;

@Service
public class UserService {
	
	@Autowired
	private IUserRepository userRepo;
	
	public void save(User user) {
		userRepo.save(user);
	}
	
	public boolean findByUsername(User user) {
		User userFromRepo = userRepo.findByUsername(user.getUsername());
		return userFromRepo.getUsername().equals(user.getUsername())
				&& userFromRepo.getPwd().equals(user.getPwd());
	}
}
