package com.postman.calendify.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlotController {

	@GetMapping("/slots")
	public String getSlots(HttpSession session) {
		if (session.isNew()) {
			return "Please login and try again";
		}
		return "User Signed Up";
	}
	
	@GetMapping("/checkSession")
	public String getSessionId(HttpSession session) {
		return session.getId();
	}
}
