package com.postman.calendify.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class BookSlot {
	@NotNull
	@Pattern(
		    regexp = "((1[0-9])|(0?[1-9])|(2[0-4])):([0-5][0-9])", 
		    message = "Invalid Time format."
		    ) 
	private String startTime;
	
	@NotNull
	@Pattern(
		    regexp = "((1[0-9])|(0?[1-9])|(2[0-4])):([0-5][0-9])", 
		    message = "Invalid Time format."
		    )
	private String endTime;
	
	@NotNull
	@Pattern(
		    regexp = "[0-9]{4}-((0[1-9])|(1[1-2]))-(([0-2][1-9])|(31)|(10)|(20))", 
		    message = "Invalid date format."
		    )
	private String date;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
