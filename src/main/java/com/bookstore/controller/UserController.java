package com.bookstore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.bookstore.dto.*;
import com.bookstore.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public List<UserResponse> findAll() {
		return userService.findAllUsers();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse create(@RequestBody UserCreateRequest request) {
		return userService.createUser(request);
	}
	
}
