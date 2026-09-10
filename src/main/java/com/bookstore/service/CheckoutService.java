package com.bookstore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.dto.CheckoutResponse;
import com.bookstore.entity.*;
import com.bookstore.exception.BusinessRuleException;
import com.bookstore.repository.*;

@Service
public class CheckoutService {
	private final CurrentUserService currentUserService;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final BookRepository bookRepository;
	public CheckoutService(CurrentUserService currentUserService, CartRepository cartRepository,
			CartItemRepository cartItemRepository, OrderRepository orderRepository,
			OrderItemRepository orderItemRepository, BookRepository bookRepository) {
		super();
		this.currentUserService = currentUserService;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.bookRepository = bookRepository;
	}
	
	@Transactional
	public CheckoutResponse checkout() {
		User user = currentUserService.getCurrentUser();
		
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> {
			throw new BusinessRuleException("Cart does not exist");
		});
		
		List<CartItem> cartItems = cartItemRepository.findByCartOrderByIdAsc(cart);
		if (cartItems.isEmpty()) {
			throw new BusinessRuleException("Cart is empty");
		}
		
		// Validate all cart items
		for (CartItem item : cartItems) {
			Book book = item.getBook();
			if (!book.isActive()) {
				throw new BusinessRuleException ("Book is not available" + book.getTitle());
			}
			
			if (item.getQuantity() > book.getStockQuantity()) {
				throw new BusinessRuleException ("Not enough book in stock" + book.getTitle());
			}
		}
		
		// calculate total amount of cart items
		// FOR loop calculate amount of cart items
		
//		BigDecimal total = BigDecimal.ZERO;
//		for (CartItem item : cartItems) {
//			Book book = item.getBook();
//			BigDecimal totalAmountPrice = BigDecimal.valueOf(item.getQuantity()).multiply(book.getPrice());
//			total.add(totalAmountPrice);
//		}
		
		
		// stream -> map -> reduce
		BigDecimal totalAmount = cartItems.stream().map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		// 3. Create order
		Order order = new Order();
		order.setUser(user);
		order.setStatus(OrderStatus.PENDING);
		order.setTotalAmount(totalAmount);
		order.setCreatedAt(LocalDateTime.now());
		
		Order savedOrder = orderRepository.save(order);
		
		// 4. Create order items
		for (CartItem item : cartItems) {
			Book book = item.getBook();
			BigDecimal unitPrice = book.getPrice();
			BigDecimal totalLine = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
			
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(savedOrder);
			orderItem.setBook(book);
			orderItem.setBookTitle(book.getTitle());
			orderItem.setUnitPrice(unitPrice);
			orderItem.setQuantity(item.getQuantity());
			orderItem.setLineTotal(totalLine);
			
			orderItemRepository.save(orderItem);
			
			// 5. reduce book stock
			book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
			bookRepository.save(book);
		}
		
		cartItemRepository.deleteAll(cartItems);
		
		return new CheckoutResponse(savedOrder.getId(), savedOrder.getStatus().name(), savedOrder.getTotalAmount(), savedOrder.getCreatedAt());
	}
}
