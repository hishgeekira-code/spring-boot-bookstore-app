package com.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;

@Service
public class AuthorService {
	
	private final AuthorRepository authorRepository;
	
	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}
	
	public List<Author> findAllAuthors() {
		return authorRepository.findAll();
	}
	
	public Author findAuthorById(Long id) {
		return authorRepository.findById(id).orElseThrow();
	}
	
	public Author createAuthor(Author author) {
		return authorRepository.save(author);
	}
	
	public Author updateAuthor(Long id, Author newAuthor) {
		Author foundAuthor = authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found with ID: " + id));
		foundAuthor.setFirstName(newAuthor.getFirstName());
		foundAuthor.setLastName(newAuthor.getLastName());
		foundAuthor.setBio(newAuthor.getBio());
		return authorRepository.save(foundAuthor);
	}
	
	public void deleteAuthor(Long id) {
		authorRepository.deleteById(id);
	}
}
