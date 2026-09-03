package com.bookstore.dto;

import com.bookstore.model.Role;

public record UserResponse (Long id,
		String firstName,
		String lastName, String email, Role role, boolean enebled) {
	
	
	
	
}
