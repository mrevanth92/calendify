package com.postman.calendify.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postman.calendify.models.User;
import com.postman.calendify.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public String userSignup(@RequestBody User user, HttpSession session) {
		userService.save(user);
		return "User Signed Up";
	}
	
	@PostMapping("/signin")
	public String userSignin(@RequestBody User user, HttpSession session) {
		if (userService.findByUsername(user)) {
			session.setAttribute("username", user.getUsername());
			return "User has signed in";
		}
		return "Username or Password is wrong";
	}
	
	@DeleteMapping("/signout")
	public String userSignout(HttpSession session) {
		if (session.isNew()) {
			return "No user has signed in";
		}
		session.invalidate();
		return "User logged out";
	}
}
