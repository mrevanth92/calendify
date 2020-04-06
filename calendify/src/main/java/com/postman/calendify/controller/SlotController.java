package com.postman.calendify.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postman.calendify.exception.CalendifyException;
import com.postman.calendify.models.UserEntry;
import com.postman.calendify.service.UserEntryService;

@RestController
public class SlotController {

	@Autowired
	public UserEntryService userEntryService;

	@PostMapping("/addSlots")
	public String addSlots(@RequestBody UserEntry userEntry, HttpSession httpSession) throws CalendifyException {
		if (userEntryService.addSlots(userEntry.getSlots(), httpSession)) {
			return "Sessions added successfully";
		}
		return "";
	}

	@GetMapping("/bookslot/{therapistUsername}/{date}/{startTime}/{endTime}")
	public String bookSlots(@PathVariable("therapistUsername") String therapistUsername,
			@PathVariable("date") String bookDate, @PathVariable("startTime") String startTime,
			@PathVariable("endTime") String endTime, HttpSession httpSesssion) throws CalendifyException {
		if (userEntryService.bookSlots(therapistUsername, bookDate, startTime, endTime, httpSesssion)) {
			return "Session Booked";
		}
		return "Session not available";
	}
}
