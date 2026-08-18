package com.bookstore.service;

import com.bookstore.dto.CheckoutForm;
import com.bookstore.entity.Book;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, BookRepository bookRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.cartService = cartService;
    }

    @Transactional
    public Order placeOrder(CheckoutForm form, HttpSession session) {
        List<CartService.CartLine> lines = cartService.getLines(session);
        if (lines.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your cart is empty");
        }

        for (CartService.CartLine line : lines) {
            if (line.quantity() > line.book().getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough stock for \"" + line.book().getTitle() + "\" (available: " + line.book().getStock() + ")");
            }
        }

        Order order = new Order();
        order.setCustomerName(form.getName().trim());
        order.setCustomerEmail(form.getEmail().trim());
        order.setCustomerAddress(form.getAddress().trim());
        order.setOrderDate(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (CartService.CartLine line : lines) {
            Book book = line.book();
            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(line.quantity());
            item.setUnitPrice(book.getPrice());
            order.addItem(item);

            total = total.add(line.subtotal());
            book.setStock(book.getStock() - line.quantity());
            bookRepository.save(book);
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        cartService.clear(session);
        return saved;
    }

    public Order getById(Long id) {
        return orderRepository.findWithItemsById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public List<Order> findForEmail(String email) {
        return orderRepository.findByCustomerEmailWithItems(email);
    }
}
