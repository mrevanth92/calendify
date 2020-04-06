package com.postman.calendify.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postman.calendify.models.Slot;

public interface ISlotRepository extends JpaRepository<Slot, Long> {
	public List<Slot> findAllByBookDateAndStartTimeAndEndTimeAndBookedAndUserEntryId(LocalDate date, LocalTime startTime, LocalTime endTime, boolean booked, long userid);

}
