package com.bookstore.controller;

import org.springframework.web.bind.annotation.*;

import com.bookstore.dto.StripeCheckoutResponse;
import com.bookstore.service.StripePaymentService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/api/customer/orders")
public class CustomerPaymentController {
	
	private final StripePaymentService stripePaymentService;
	
	public CustomerPaymentController(StripePaymentService stripePaymentService) {
		this.stripePaymentService = stripePaymentService;
	}
	
	@PostMapping("/{orderId}/payment")
	public StripeCheckoutResponse createPayment(@PathVariable Long orderId) throws StripeException {
		return stripePaymentService.createCheckoutSession(orderId);
	}
}
