package com.postman.calendify.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postman.calendify.models.UserEntry;

public interface IUserEntryRepository extends JpaRepository<UserEntry, Long> {
	
	public UserEntry findByUsername(String username);
	
	public UserEntry findByUsernameAndSlotsBookDate(String username, LocalDate date);
	
	public UserEntry findByUsernameAndSlotsBookDateAndSlotsStartTimeGreaterThanEqual(String username, LocalDate date, LocalTime time);
}
