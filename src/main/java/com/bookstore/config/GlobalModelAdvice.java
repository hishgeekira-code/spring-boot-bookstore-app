package com.bookstore.config;

import com.bookstore.service.BookService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAdvice {

    private final BookService bookService;

    public GlobalModelAdvice(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("categories")
    public List<String> categories() {
        return bookService.listCategories();
    }
}
