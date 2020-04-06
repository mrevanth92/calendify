package com.postman.calendify.controller;

import java.time.LocalDate;
import java.time.LocalTime;

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

	@GetMapping("/availableslots/{username}/{date}")
	public int getAvaialableSlots(@PathVariable("username") String username, @PathVariable String date, HttpSession session) {
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
		String userRole = (String) httpSesssion.getAttribute("userrole");
		if(userRole.equals("therapist")) {
			String username = (String) usernameObj;
			long userId = (long) userIdObj;
			userEntryService.addSlots(userEntry.getSlots(), username, userId);
			return "Sessions added successfully";
		}
		return "You don't have permission to add sessions";
	}
	
	@GetMapping("/bookslot/{therapistUsername}/{date}/{startTime}/{endTime}")
	public String bookSlots(@PathVariable("therapistUsername") String therapistUsername,
			@PathVariable("date") String bookDate, 
			@PathVariable("startTime") String startTime,
			@PathVariable("endTime") String endTime,
			HttpSession httpSesssion) {
		Object usernameObj = httpSesssion.getAttribute("username");
		if (httpSesssion.isNew() || usernameObj == null) {
			return "Please login and try again";
		}
		String username = (String) usernameObj;
		if (username.equals(therapistUsername)) {
			return "Can not book your own session";
		}
		userEntryService.bookSlots(username, therapistUsername, bookDate, startTime, endTime);
		return "Session Booked";
	}
}
