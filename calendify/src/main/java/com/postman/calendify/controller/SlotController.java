package com.postman.calendify.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postman.calendify.models.UserEntry;
import com.postman.calendify.service.UserEntryService;

@RestController
public class SlotController {
	
	@Autowired
	public UserEntryService userEntryService;

	@GetMapping("/slots")
	public String getSlots(HttpSession session) {
		if (session.isNew()) {
			return "Please login and try again";
		}
		return "User Signed Up";
	}

	@GetMapping("/availableslots/{username}/{date}")
	public int getAvaialableSlots(@PathVariable("username") String username, @PathVariable String date, HttpSession session) {
		if (session.isNew()) {
			return 0;
		}
		try {
			return userEntryService.availableSlots(username, date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@PostMapping("/addSlots")
	public String addSlots(@RequestBody UserEntry userEntry, HttpSession httpSesssion) {
		Object usernameObj = httpSesssion.getAttribute("username");
		Object userIdObj = httpSesssion.getAttribute("userid");
		if(httpSesssion.isNew() || usernameObj == null || userIdObj == null) {
			return "Please login and try again";
		}
		String username = (String) usernameObj;
		long userId = (long) userIdObj;
		String userRole = (String) httpSesssion.getAttribute("userrole");
		userEntry.setUsername(username);
		if(userRole.equals("therapist")) {
			userEntryService.addSlots(userEntry.getSlots(), username, userId);
			return "Slots added successfully";
		}
		return "You don't have permission to add slots";
	}

	@GetMapping("/checkSession")
	public String getSessionId(HttpSession session) {
		return session.getId();
	}
}
