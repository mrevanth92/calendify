package com.postman.calendify.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postman.calendify.models.Book;
import com.postman.calendify.models.Slot;
import com.postman.calendify.models.UserEntry;
import com.postman.calendify.repository.IBookRepository;
import com.postman.calendify.repository.ISlotRepository;
import com.postman.calendify.repository.IUserEntryRepository;

@Service
public class UserEntryService {

	private final static String DATE_PATTERN = "[0-9]{4}-((0[1-9])|(1[1-2]))-(([0-2][1-9])|(31))";
	private final static String TIME_PATTERN = "((1[0-9])|(0?[1-9])|(2[0-4])):([0-5][0-9])";

	@Autowired
	private IUserEntryRepository userEntryRepo;

	@Autowired
	private ISlotRepository slotRepo;

	@Autowired
	private IBookRepository bookRepo;

	public void addSlots(List<Slot> slots, String username, long userId) {
		UserEntry userEntry = userEntryRepo.findByUsername(username);
		if (userEntry == null) {
			userEntry = new UserEntry(userId, username);
			userEntryRepo.save(userEntry);
		}
		Set<Slot> duplicateCheck = new HashSet<>();
		for (Slot slot : slots) {
			slot.setUserEntry(userEntry);
			UserEntry tempUser = userEntryRepo.findByUsernameAndSlotsBookDateAndSlotsStartTimeGreaterThanEqual(
					userEntry.getUsername(), slot.getBookDate(), slot.getStartTime());
			if (tempUser != null && checkOverlap(tempUser.getSlots(), slot.getStartTime(), slot.getEndTime())) {
				// add error
			} else if (!duplicateCheck.add(slot)) {
				// add error
			} else if (slot.getStartTime().getHour() + 1 == slot.getEndTime().getHour()
					&& slot.getStartTime().getMinute() == slot.getEndTime().getMinute()) {
				slotRepo.save(slot);
			} else {
				// add error- not one hour slots
			}
		}
	}

	public int availableSlots(String username, String date) throws Exception {
		try {
			LocalDate bookDate = LocalDate.parse(date);
			UserEntry entry = userEntryRepo.findByUsernameAndSlotsBookDate(username, bookDate);
			if (entry == null || entry.getSlots() == null) {
				return 0;
			}
			return entry.getSlots().size();
		} catch (DateTimeParseException ex) {
			throw new Exception("Date is not in yyyy-MM-dd format.");// add
																		// error
		}
	}

	public void bookSlots(String username, String therapistName, String date, String startTime, String endTime) {
		if (!checkPattern(DATE_PATTERN, date)) {
			// add error
		} else if (!checkPattern(TIME_PATTERN, startTime) || !checkPattern(TIME_PATTERN, endTime)) {
			// add error
		} else {
			UserEntry userEntry = userEntryRepo.findByUsername(therapistName);
			if (userEntry != null) {
				List<Slot> slots = slotRepo.findAllByBookDateAndStartTimeAndEndTimeAndBookedAndUserEntryId(
						LocalDate.parse(date), LocalTime.parse(startTime), LocalTime.parse(endTime), false,
						userEntry.getId());
				if (slots != null) {
					for (Slot slot : slots) {
						slot.setBooked(true);
						slotRepo.save(slot);
						Book book = new Book(username, slot);
						bookRepo.save(book);
					}
				} else {
					// add error - session not available
				}
			} else {
				// add error - session not available
			}
		}
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
}
