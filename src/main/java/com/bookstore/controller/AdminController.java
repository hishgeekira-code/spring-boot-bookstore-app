package com.bookstore.controller;

import com.bookstore.dto.BookForm;
import com.bookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "admin/book-list";
    }

    @GetMapping("/books/new")
    public String newForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "admin/book-form";
    }

    @PostMapping("/books")
    public String create(@Valid @ModelAttribute("bookForm") BookForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (binding.hasErrors()) {
            return "admin/book-form";
        }
        try {
            bookService.create(form.toEntity());
        } catch (ResponseStatusException e) {
            binding.rejectValue("isbn", "isbn.exists", e.getReason());
            return "admin/book-form";
        }
        redirectAttributes.addFlashAttribute("flash", "Book added");
        return "redirect:/admin";
    }

    @GetMapping("/books/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("bookForm", BookForm.from(bookService.getById(id)));
        return "admin/book-form";
    }

    @PostMapping("/books/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("bookForm") BookForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (binding.hasErrors()) {
            return "admin/book-form";
        }
        try {
            bookService.update(id, form.toEntity());
        } catch (ResponseStatusException e) {
            binding.rejectValue("isbn", "isbn.exists", e.getReason());
            return "admin/book-form";
        }
        redirectAttributes.addFlashAttribute("flash", "Book updated");
        return "redirect:/admin";
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("flash", "Book deleted");
        return "redirect:/admin";
    }
}
