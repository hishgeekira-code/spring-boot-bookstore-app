package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	boolean existsByIsbn(String isbn);
	
	boolean existsByIsbnAndIdNot(String isbn, Long id);
}
