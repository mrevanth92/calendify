package com.postman.calendify.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postman.calendify.exception.CalendifyException;
import com.postman.calendify.models.User;
import com.postman.calendify.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public String userSignup(@RequestBody User user, HttpSession session) throws CalendifyException{
		userService.save(user,session);
		return "User Signed Up";
	}
	
	@PostMapping("/signin")
	public String userSignin(@RequestBody User user, HttpSession session) throws CalendifyException {
		if (userService.signin(user, session)) {
			return "User is signed in";
		}
		return "";
	}
	
	@DeleteMapping("/signout")
	public String userSignout(HttpSession session) throws CalendifyException {
		userService.signout(session);
		return "User logged out";
	}
}
