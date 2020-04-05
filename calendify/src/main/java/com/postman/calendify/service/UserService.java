package com.postman.calendify.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postman.calendify.models.User;
import com.postman.calendify.repository.IUserRepository;

@Service
public class UserService {
	
	private static Set<String> roles = new HashSet<>();
	
	static{
		roles.add("therapist");
		roles.add("patient");
	}
	
	@Autowired
	private IUserRepository userRepo;
	
	public void save(User user) {
		if(roles.contains(user.getRole())) {
			userRepo.save(user);
		}		
	}
	
	public boolean authenticateUser(User user) {
		User userFromRepo = userRepo.findByUsername(user.getUsername());
		if (userFromRepo != null) {
			user.setId(userFromRepo.getId());
			user.setRole(userFromRepo.getRole());
		}
		return userFromRepo != null && userFromRepo.getUsername().equals(user.getUsername())
				&& userFromRepo.getPwd().equals(user.getPwd());
	}
}
