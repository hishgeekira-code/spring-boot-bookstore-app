package com.bookstore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.bookstore.entity.Category;
import com.bookstore.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public List<Category> findAllCategories() {
		return categoryService.findAllCategories();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Category create(@RequestBody Category category) {
		return categoryService.createCategory(category);
	}
	
	@GetMapping("/{id}")
	public Category findById(@PathVariable Long id) {
		return categoryService.findCategoryById(id);
	}
	
	@PutMapping("/{id}")
	public Category update(@RequestBody Category category, @PathVariable Long id) {
		return categoryService.updateCategory(id, category);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		categoryService.deleteCategory(id);
	}
}
