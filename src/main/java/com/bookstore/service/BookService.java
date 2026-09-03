package com.bookstore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bookstore.dto.*;
import com.bookstore.entity.*;
import com.bookstore.repository.*;

@Service
public class BookService {
	
	private final BookRepository bookRepository;
	private final CategoryRepository categoryRepository;
	private final AuthorRepository authorRepository;
	
	public BookService(BookRepository bookRepository, CategoryRepository categoryRepository, AuthorRepository authorRepository) {
		this.bookRepository = bookRepository;
		this.categoryRepository = categoryRepository;
		this.authorRepository = authorRepository;
	}
	
	public List<BookResponse> findAllBooks() {
		return bookRepository.findAll()
	            .stream()
	            .map(this::toResponse)
	            .toList();
	}
	
	public BookResponse createBook (BookRequest request) {
		if (bookRepository.existsByIsbn(request.isbn())) {
			throw new RuntimeException("Book already exists with ISBN: " + request.isbn());
		}
		
		Category foundCategory = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.categoryId()));
		Author foundAuthor = authorRepository.findById(request.authorId()).orElseThrow(() -> new RuntimeException("Author not found with ID: " + request.authorId()));
		
		Book newBook = new Book();
		newBook.setIsbn(request.isbn());
		newBook.setTitle(request.title());
		newBook.setPrice(request.price());
		newBook.setStockQuantity(request.stockQuantity());
		newBook.setActive(request.active());
		newBook.setCategory(foundCategory);
		newBook.setAuthor(foundAuthor);
		
		Book savedBook = bookRepository.save(newBook);
		return toResponse(savedBook);
	}
	
	public BookResponse updateBook(Long id, BookRequest request) {
		Book foundBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
		Category foundCategory = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.categoryId()));
		Author foundAuthor = authorRepository.findById(request.authorId()).orElseThrow(() -> new RuntimeException("Author not found with ID: " + request.authorId()));
		
		if (bookRepository.existsByIsbn(request.isbn()) && !foundBook.getIsbn().equals(request.isbn())) {
			throw new RuntimeException("Book already exists with ISBN: " + request.isbn());
		}
		
		foundBook.setTitle(request.title());
		foundBook.setActive(request.active());
		foundBook.setAuthor(foundAuthor);
		foundBook.setCategory(foundCategory);
		foundBook.setIsbn(request.isbn());
		foundBook.setPrice(request.price());
		foundBook.setStockQuantity(request.stockQuantity());
		
		Book updateBook = bookRepository.save(foundBook);
		return toResponse(updateBook);
	}
	
	public void deleteBook(Long id) {
		Book foundBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
		bookRepository.delete(foundBook);
	}
	
	public Page<BookResponse> findShopBooks(String keyword, Long categoryId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Book> books;
		
		boolean hasKeyword = keyword != null && !keyword.isBlank();
		boolean hasCategory = categoryId != null;
		
		if (hasKeyword && hasCategory) {
			books = bookRepository.findByActiveTrueAndTitleContainingIgnoreCaseAndCategoryId(keyword.trim(), categoryId, pageable);
		} else if (hasKeyword) {
			books = bookRepository.findByActiveTrueAndTitleContainingIgnoreCase(keyword.trim(), pageable);
		} else if (hasCategory) {
			books = bookRepository.findByActiveTrueAndCategoryId(categoryId, pageable);
		} else {
			books = bookRepository.findByActiveTrue(pageable);
		}
		
		return books.map(this::toResponse);
	}
	
	public BookResponse findActiveBookById(Long id) {
		Book book = bookRepository.findById(id).orElseThrow();
		
		if (!book.isActive()) {
			System.out.println("Book is not active!");
		}
		
		return toResponse(book);
	}
	
	private BookResponse toResponse(Book book) {

		return new BookResponse(
		        book.getId(),
		        book.getTitle(),
		        book.getIsbn(),
		        book.getPrice(),
		        book.getStockQuantity(),
		        book.isActive(),
		        
		        book.getCategory().getId(),
		        book.getCategory().getName(),
		        
		        book.getAuthor().getId(),
		        book.getAuthor().getFirstName(),
		        book.getAuthor().getLastName()
				);
	}

	
}
