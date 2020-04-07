package com.postman.calendify.models;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "slot")
public class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	public long id;
	
	@Column(nullable = false)
	public LocalDate bookDate;
	
	@Column(nullable = false)
	public LocalTime startTime;
	
	@Column(nullable = false)
	public LocalTime endTime;
	
	public boolean booked;
	
	@OneToOne(mappedBy = "slot", cascade = CascadeType.ALL)
	private Book book;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public UserEntry userEntry;

	public Slot() {
		super();
	}

	public Slot(long id, LocalDate date, LocalTime startTime, LocalTime endTime, UserEntry userEntry) {
		super();
		this.id = id;
		this.bookDate = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.userEntry = userEntry;
		this.booked = false;
	}

	public Slot(LocalDate date, LocalTime startTime, LocalTime endTime, UserEntry userEntry) {
		super();
		this.bookDate = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.userEntry = userEntry;
		this.booked =  false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getBookDate() {
		return bookDate;
	}

	public void setBookDate(LocalDate date) {
		this.bookDate = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public UserEntry getUserEntry() {
		return userEntry;
	}

	public void setUserEntry(UserEntry userEntry) {
		this.userEntry = userEntry;
	}

	public boolean isBooked() {
		return booked;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + ((bookDate == null) ? 0 : bookDate.hashCode());
		result = prime * result + (booked ? 1231 : 1237);
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((userEntry == null) ? 0 : userEntry.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Slot other = (Slot) obj;
		if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.book))
			return false;
		if (bookDate == null) {
			if (other.bookDate != null)
				return false;
		} else if (!bookDate.equals(other.bookDate))
			return false;
		if (booked != other.booked)
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id != other.id)
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (userEntry == null) {
			if (other.userEntry != null)
				return false;
		} else if (!userEntry.equals(other.userEntry))
			return false;
		return true;
	}
}
