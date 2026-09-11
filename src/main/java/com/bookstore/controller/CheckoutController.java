package com.bookstore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.bookstore.dto.CheckoutResponse;
import com.bookstore.service.CheckoutService;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
	
	private final CheckoutService checkoutService;
	
	public CheckoutController(CheckoutService checkoutService) {
		this.checkoutService = checkoutService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CheckoutResponse checkout() {
		return checkoutService.checkout();
	}
}
