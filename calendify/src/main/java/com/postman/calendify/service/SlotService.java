package com.postman.calendify.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postman.calendify.exception.CalendifyException;
import com.postman.calendify.models.Book;
import com.postman.calendify.models.BookSlot;
import com.postman.calendify.models.Roles;
import com.postman.calendify.models.Slot;
import com.postman.calendify.models.UserEntry;
import com.postman.calendify.repository.IBookRepository;
import com.postman.calendify.repository.ISlotRepository;
import com.postman.calendify.repository.IUserEntryRepository;

@Service
public class SlotService {

	private final static String DATE_PATTERN = "[0-9]{4}-((0[1-9])|(1[1-2]))-(([0-2][1-9])|(31)|(10)|(20))";
	private final static String TIME_PATTERN = "((1[0-9])|(0?[1-9])|(2[0-4])):([0-5][0-9])";

	@Autowired
	private IUserEntryRepository userEntryRepo;

	@Autowired
	private ISlotRepository slotRepo;

	@Autowired
	private IBookRepository bookRepo;

	public boolean addSlots(List<Slot> slots, HttpSession httpSession) throws CalendifyException {
		loginCheck(httpSession);
		Object usernameObj = httpSession.getAttribute("username");
		Object userIdObj = httpSession.getAttribute("userid");
		String userRole = (String) httpSession.getAttribute("userrole");
		if (userRole.equals(Roles.THERAPIST.getRole())) {
			String username = (String) usernameObj;
			long userId = (long) userIdObj;
			UserEntry userEntry = userEntryRepo.findByUsername(username);
			if (userEntry == null) {
				userEntry = new UserEntry(userId, username);
				userEntryRepo.save(userEntry);
			}
			Set<Slot> duplicateCheck = new HashSet<>();
			for (Slot slot : slots) {
				if(slot.getBookDate() == null || slot.getStartTime() == null || slot.getEndTime() == null) {
					throw new CalendifyException(422, "Session missing required fields");
				}
				slot.setUserEntry(userEntry);
				UserEntry tempUser = userEntryRepo.findByUsernameAndSlotsBookDateAndSlotsStartTimeGreaterThanEqual(
						userEntry.getUsername(), slot.getBookDate(), slot.getStartTime());
				if (tempUser != null && checkOverlap(tempUser.getSlots(), slot.getStartTime(), slot.getEndTime())) {
					throw new CalendifyException(422, "Session time slots overlap");
				} else if (!duplicateCheck.add(slot)) {
					throw new CalendifyException(422, "Duplicate Session time Slots");
				} else if (!(slot.getStartTime().getHour() + 1 == slot.getEndTime().getHour()
						&& slot.getStartTime().getMinute() == slot.getEndTime().getMinute())) {
					throw new CalendifyException(422, "Session time slot greater than 1 hour");
				}
			}
			for (Slot slot:slots) {
				slotRepo.save(slot);
			}
			return true;
		}
		throw new CalendifyException(403, "You don't have permission to add sessions");
	}

	public boolean bookSlots(String therapistName, BookSlot bookSlot, HttpSession httpSession)
			throws CalendifyException {
		loginCheck(httpSession);
		String username = (String) httpSession.getAttribute("username");
		if (username.equals(therapistName)) {
			throw new CalendifyException(422, "Can not book your own session");
		}
		if(bookSlot.getDate() == null || bookSlot.getEndTime() == null || bookSlot.getStartTime() == null) {
			throw new CalendifyException(422, "Missing required fields. Required Fields : BookDate,EndTime,StartTime.");
		}
		if (!checkPattern(DATE_PATTERN, bookSlot.getDate())) {
			throw new CalendifyException(422, "Invalid date format.Valid Date Format:yyyy-MM-dd");
		} else if (!checkPattern(TIME_PATTERN, bookSlot.getStartTime()) || !checkPattern(TIME_PATTERN, bookSlot.getEndTime())) {
			throw new CalendifyException(422, "Invalid time format.Valid Time Format:HH:mm");
		} else {
			UserEntry userEntry = userEntryRepo.findByUsername(therapistName);
			if (userEntry != null) {
				List<Slot> slots = slotRepo.findAllByBookDateAndStartTimeAndEndTimeAndBookedAndUserEntryId(
						LocalDate.parse(bookSlot.getDate()), LocalTime.parse(bookSlot.getStartTime()), LocalTime.parse(bookSlot.getEndTime()), false,
						userEntry.getId());
				if (slots != null && !slots.isEmpty()) {
					for (Slot slot : slots) {
						slot.setBooked(true);
						slotRepo.save(slot);
						Book book = new Book(username, slot);
						bookRepo.save(book);
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean checkOverlap(List<Slot> slots, LocalTime startTime, LocalTime endTime) {
		for (Slot slot : slots) {
			if ((slot.getStartTime().isBefore(startTime) && slot.getEndTime().isAfter(startTime))
					|| (slot.getStartTime().isBefore(endTime) && slot.getEndTime().isAfter(endTime))) {
				return true;
			}
			if (slot.getEndTime().equals(endTime) && slot.getStartTime().equals(startTime)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkPattern(String pattern, String value) {
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(value);
		return m.matches();
	}

	private void loginCheck(HttpSession httpSession) throws CalendifyException {
		Object usernameObj = httpSession.getAttribute("username");
		Object userIdObj = httpSession.getAttribute("userid");
		if (httpSession.isNew() || usernameObj == null || userIdObj == null) {
			throw new CalendifyException(403, "Please login and try again");
		}
	}
}
