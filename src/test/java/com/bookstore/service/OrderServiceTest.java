package com.bookstore.service;

import com.bookstore.dto.CheckoutForm;
import com.bookstore.entity.Book;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    CartService cartService;

    @Mock
    HttpSession session;

    @InjectMocks
    OrderService orderService;

    Book book1;
    Book book2;
    CheckoutForm form;

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert C. Martin");
        book1.setPrice(new BigDecimal("38.95"));
        book1.setStock(10);

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("The Great Gatsby");
        book2.setAuthor("F. Scott Fitzgerald");
        book2.setPrice(new BigDecimal("19.99"));
        book2.setStock(5);

        form = new CheckoutForm();
        form.setName("John Doe");
        form.setEmail("john@example.com");
        form.setAddress("123 Main St");
    }

    @Test
    void placeOrder_happyPath_savesOrderDecrementsStockAndClearsCart() {
        var line1 = new CartService.CartLine(book1, 2, book1.getPrice().multiply(BigDecimal.valueOf(2)));
        var line2 = new CartService.CartLine(book2, 1, book2.getPrice());
        lenient().when(cartService.getLines(session)).thenReturn(List.of(line1, line2));
        lenient().when(cartService.isEmpty(session)).thenReturn(false);
        lenient().when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(100L);
            return o;
        });

        Order placed = orderService.placeOrder(form, session);

        assertThat(placed.getId()).isEqualTo(100L);
        assertThat(placed.getCustomerName()).isEqualTo("John Doe");
        assertThat(placed.getCustomerEmail()).isEqualTo("john@example.com");
        assertThat(placed.getTotalAmount()).isEqualTo(new BigDecimal("97.89"));
        assertThat(book1.getStock()).isEqualTo(8);
        assertThat(book2.getStock()).isEqualTo(4);
        verify(cartService).clear(session);
    }

    @Test
    void placeOrder_emptyCart_throwsBadRequest() {
        lenient().when(cartService.isEmpty(session)).thenReturn(true);

        assertThatThrownBy(() -> orderService.placeOrder(form, session))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void placeOrder_insufficientStock_throwsBadRequest() {
        book1.setStock(1);
        var line1 = new CartService.CartLine(book1, 2, book1.getPrice().multiply(BigDecimal.valueOf(2)));
        lenient().when(cartService.getLines(session)).thenReturn(List.of(line1));
        lenient().when(cartService.isEmpty(session)).thenReturn(false);

        assertThatThrownBy(() -> orderService.placeOrder(form, session))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void getById_whenExists_returnsOrder() {
        Order order = new Order();
        order.setId(100L);
        order.setCustomerName("John");
        order.setCustomerEmail("john@example.com");
        order.addItem(new OrderItem());
        when(orderRepository.findWithItemsById(100L)).thenReturn(Optional.of(order));

        Order found = orderService.getById(100L);

        assertThat(found.getId()).isEqualTo(100L);
    }

    @Test
    void getById_whenNotFound_throwsNotFound() {
        when(orderRepository.findWithItemsById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getById(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void findForEmail_returnsOrdersWithItems() {
        Order order = new Order();
        order.setId(100L);
        order.setCustomerEmail("john@example.com");
        order.addItem(new OrderItem());
        when(orderRepository.findByCustomerEmailWithItems("john@example.com")).thenReturn(List.of(order));

        List<Order> orders = orderService.findForEmail("john@example.com");

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getCustomerEmail()).isEqualTo("john@example.com");
    }
}