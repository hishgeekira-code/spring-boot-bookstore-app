package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    HttpSession session;

    @InjectMocks
    CartService cartService;

    Book book1;
    Book book2;
    Map<Long, Integer> cartMap;

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert C. Martin");
        book1.setPrice(new BigDecimal("38.95"));
        book1.setStock(15);

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("The Great Gatsby");
        book2.setAuthor("F. Scott Fitzgerald");
        book2.setPrice(new BigDecimal("19.99"));
        book2.setStock(12);

        cartMap = new HashMap<>();
        lenient().when(session.getAttribute(eq("cart"))).thenReturn(cartMap);
        lenient().when(session.getAttribute(eq("cartCount"))).thenReturn(0);
    }

    @Test
    void addItem_firstAdd_createsCartAndSetsCount() {
        cartService.addItem(session, 1L, 2);

        assertThat(cartMap).containsEntry(1L, 2);
        verify(session).setAttribute(eq("cartCount"), eq(2));
    }

    @Test
    void addItem_existingEntry_incrementsQuantity() {
        cartMap.put(1L, 1);

        cartService.addItem(session, 1L, 3);

        assertThat(cartMap.get(1L)).isEqualTo(4);
        verify(session).setAttribute(eq("cartCount"), eq(4));
    }

    @Test
    void addItem_negativeQuantity_usesOne() {
        cartService.addItem(session, 1L, -5);

        assertThat(cartMap.get(1L)).isEqualTo(1);
    }

    @Test
    void updateItem_updatesQuantity() {
        cartMap.put(1L, 2);

        cartService.updateItem(session, 1L, 5);

        assertThat(cartMap.get(1L)).isEqualTo(5);
    }

    @Test
    void updateItem_zeroQuantity_removesItem() {
        cartMap.put(1L, 2);
        cartMap.put(2L, 1);

        cartService.updateItem(session, 1L, 0);

        assertThat(cartMap).doesNotContainKey(1L);
        assertThat(cartMap.get(2L)).isEqualTo(1);
    }

    @Test
    void removeItem_removesFromCart() {
        cartMap.put(1L, 2);
        cartMap.put(2L, 1);

        cartService.removeItem(session, 1L);

        assertThat(cartMap).doesNotContainKey(1L);
        assertThat(cartMap).containsEntry(2L, 1);
    }

    @Test
    void clear_removesCartAndCount() {
        cartMap.put(1L, 2);

        cartService.clear(session);

        verify(session).removeAttribute("cart");
        verify(session).removeAttribute("cartCount");
    }

    @Test
    void getItemCount_returnsSum() {
        cartMap.put(1L, 2);
        cartMap.put(2L, 3);
        when(session.getAttribute("cartCount")).thenReturn(5);

        int count = cartService.getItemCount(session);

        assertThat(count).isEqualTo(5);
    }

    @Test
    void getLines_returnsMappedLinesWithSubtotals() {
        cartMap.put(1L, 2);
        cartMap.put(2L, 1);
        when(bookRepository.findAllById(any())).thenReturn(List.of(book1, book2));

        var lines = cartService.getLines(session);

        assertThat(lines).hasSize(2);
        assertThat(lines.get(0).book().getId()).isEqualTo(1L);
        assertThat(lines.get(0).quantity()).isEqualTo(2);
        assertThat(lines.get(0).subtotal()).isEqualTo(new BigDecimal("77.90"));
        assertThat(lines.get(1).subtotal()).isEqualTo(new BigDecimal("19.99"));
    }

    @Test
    void getTotal_sumsSubtotals() {
        cartMap.put(1L, 2);
        when(bookRepository.findAllById(any())).thenReturn(List.of(book1));
        var lines = cartService.getLines(session);

        BigDecimal total = cartService.getTotal(lines);

        assertThat(total).isEqualTo(new BigDecimal("77.90"));
    }

    @Test
    void isEmpty_whenCartEmpty_returnsTrue() {
        when(session.getAttribute("cartCount")).thenReturn(0);

        assertThat(cartService.isEmpty(session)).isTrue();
    }

    @Test
    void isEmpty_whenCartHasItems_returnsFalse() {
        when(session.getAttribute("cartCount")).thenReturn(3);

        assertThat(cartService.isEmpty(session)).isFalse();
    }
}