package com.postman.calendify.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postman.calendify.exception.CalendifyException;
import com.postman.calendify.models.BookSlot;
import com.postman.calendify.models.UserEntry;
import com.postman.calendify.service.SlotService;

@RestController
public class SlotController {

	@Autowired
	public SlotService slotService;

	@PostMapping("/slots")
	public String addSlots(@RequestBody UserEntry userEntry, HttpSession httpSession) throws CalendifyException {
		if (slotService.addSlots(userEntry.getSlots(), httpSession)) {
			return "Sessions added successfully";
		}
		return "";
	}

	@PostMapping("/slots/{therapistUsername}/book")
	public String bookSlots(@PathVariable("therapistUsername") String therapistUsername,
			@RequestBody BookSlot bookSlot, HttpSession httpSesssion) throws CalendifyException {
		if (slotService.bookSlots(therapistUsername, bookSlot, httpSesssion)) {
			return "Session Booked";
		}
		return "Session not available";
	}
}
