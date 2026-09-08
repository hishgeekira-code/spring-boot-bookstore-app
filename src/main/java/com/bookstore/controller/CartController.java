package com.bookstore.controller;

import org.springframework.web.bind.annotation.*;

import com.bookstore.dto.AddCartItemRequest;
import com.bookstore.dto.CartResponse;
import com.bookstore.dto.UpdateCartItemRequest;
import com.bookstore.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	private final CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@GetMapping
	public CartResponse getCart() {
		return cartService.getCurrentCart();
	}
	
	@PostMapping("/items")
	public CartResponse addItem(@Valid @RequestBody AddCartItemRequest request) {
		return cartService.addItem(request);
	}
	
	@PutMapping("/items/{id}")
	public CartResponse updateQuantity(@PathVariable Long id, @Valid @RequestBody UpdateCartItemRequest request) {
		return cartService.updateQuantity(id, request);
	}
	
	@DeleteMapping("/items/{id}")
	public CartResponse removeItem(@PathVariable Long id) {
		return cartService.removeItem(id);
	}
}
