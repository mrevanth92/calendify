package com.postman.calendify.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postman.calendify.models.Slot;
import com.postman.calendify.models.UserEntry;
import com.postman.calendify.repository.ISlotRepository;
import com.postman.calendify.repository.IUserEntryRepository;

@Service
public class UserEntryService {

	@Autowired
	private IUserEntryRepository userEntryRepo;

	@Autowired
	private ISlotRepository slotRepo;

	public void addSlots(List<Slot> slots, String username, long userId) {
		UserEntry userEntry = userEntryRepo.findByUsername(username);
		if (userEntry == null) {
			userEntry = new UserEntry(userId, username);
			userEntryRepo.save(userEntry);
		}
		Set<Slot> duplicateCheck = new HashSet<>();
		for (Slot slot : slots) {
			slot.setUserEntry(userEntry);
			UserEntry tempUser = userEntryRepo.findByUsernameAndSlotsBookDateAndSlotsStartTimeGreaterThanEqual(userEntry.getUsername(), slot.getBookDate(), slot.getStartTime());
			if (tempUser == null || (!checkOverlap(tempUser.getSlots(), slot.getStartTime(), slot.getEndTime()) && duplicateCheck.add(slot))) {
				slotRepo.save(slot);
			} // add error
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
			throw new Exception("Date is not in yyyy-MM-dd format.");//add error
		}
	}

	private boolean checkOverlap(List<Slot> slots, LocalTime startTime, LocalTime endTime) {
		for(Slot slot : slots) {
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
}
