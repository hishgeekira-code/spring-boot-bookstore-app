package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class BookService {

    public static final List<String> ALL_CATEGORIES = List.of(
            "Fiction", "Non-Fiction", "Science", "Technology",
            "History", "Children", "Mystery", "Romance");

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> search(String query, String category, Pageable pageable) {
        String q = query == null ? "" : query.trim();
        boolean hasQuery = !q.isEmpty();
        boolean hasCategory = category != null && !category.isBlank();

        if (hasCategory && hasQuery) {
            return bookRepository.searchInCategory(category, q, pageable);
        }
        if (hasCategory) {
            return bookRepository.findByCategoryIgnoreCase(category, pageable);
        }
        if (hasQuery) {
            return bookRepository.search(q, pageable);
        }
        return bookRepository.findAll(pageable);
    }

    public List<Book> findAll() {
        return bookRepository.findAll(Sort.by("title"));
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @Transactional
    public Book create(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A book with ISBN " + book.getIsbn() + " already exists");
        }
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Long id, Book data) {
        if (bookRepository.existsByIsbnAndIdNot(data.getIsbn(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A book with ISBN " + data.getIsbn() + " already exists");
        }
        Book book = getById(id);
        book.setTitle(data.getTitle());
        book.setAuthor(data.getAuthor());
        book.setIsbn(data.getIsbn());
        book.setCategory(data.getCategory());
        book.setPrice(data.getPrice());
        book.setStock(data.getStock());
        book.setPublicationYear(data.getPublicationYear());
        book.setImageUrl(data.getImageUrl());
        book.setDescription(data.getDescription());
        return book;
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.delete(getById(id));
    }

    public List<String> listCategories() {
        Set<String> categories = new LinkedHashSet<>(ALL_CATEGORIES);
        categories.addAll(bookRepository.findDistinctCategories());
        return new ArrayList<>(categories);
    }
}
