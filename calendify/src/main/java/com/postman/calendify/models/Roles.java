package com.postman.calendify.models;

public enum Roles {
	THERAPIST("therapist"),
	PATIENT("patient");
	
	private final String role;
	
	private Roles(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
}
