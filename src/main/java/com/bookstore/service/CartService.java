package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final String CART_KEY = "cart";
    private static final String COUNT_KEY = "cartCount";

    private final BookRepository bookRepository;

    public CartService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public record CartLine(Book book, int quantity, BigDecimal subtotal) {
    }

    public void addItem(HttpSession session, Long bookId, int quantity) {
        Map<Long, Integer> cart = cart(session);
        cart.merge(bookId, Math.max(quantity, 1), Integer::sum);
        updateCount(session, cart);
    }

    public void updateItem(HttpSession session, Long bookId, int quantity) {
        Map<Long, Integer> cart = cart(session);
        if (quantity < 1) {
            cart.remove(bookId);
        } else {
            cart.put(bookId, quantity);
        }
        updateCount(session, cart);
    }

    public void removeItem(HttpSession session, Long bookId) {
        Map<Long, Integer> cart = cart(session);
        cart.remove(bookId);
        updateCount(session, cart);
    }

    public void clear(HttpSession session) {
        session.removeAttribute(CART_KEY);
        session.removeAttribute(COUNT_KEY);
    }

    public int getItemCount(HttpSession session) {
        Object count = session.getAttribute(COUNT_KEY);
        return count instanceof Integer i ? i : 0;
    }

    public boolean isEmpty(HttpSession session) {
        return getItemCount(session) == 0;
    }

    public List<CartLine> getLines(HttpSession session) {
        Map<Long, Integer> cart = cart(session);
        if (cart.isEmpty()) {
            return List.of();
        }
        Map<Long, Book> books = bookRepository.findAllById(cart.keySet()).stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        return cart.entrySet().stream()
                .filter(entry -> books.containsKey(entry.getKey()))
                .map(entry -> {
                    Book book = books.get(entry.getKey());
                    BigDecimal subtotal = book.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                    return new CartLine(book, entry.getValue(), subtotal);
                })
                .toList();
    }

    public BigDecimal getTotal(List<CartLine> lines) {
        return lines.stream()
                .map(CartLine::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Long, Integer> cart(HttpSession session) {
        Object cart = session.getAttribute(CART_KEY);
        if (!(cart instanceof Map<?, ?> map)) {
            LinkedHashMap<Long, Integer> newCart = new LinkedHashMap<>();
            session.setAttribute(CART_KEY, newCart);
            return newCart;
        }
        @SuppressWarnings("unchecked")
        Map<Long, Integer> typed = (Map<Long, Integer>) map;
        return typed;
    }

    private void updateCount(HttpSession session, Map<Long, Integer> cart) {
        int total = cart.values().stream().mapToInt(Integer::intValue).sum();
        session.setAttribute(COUNT_KEY, total);
    }
}
