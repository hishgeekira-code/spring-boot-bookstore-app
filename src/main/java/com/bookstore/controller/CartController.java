package com.bookstore.controller;

import com.bookstore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String view(HttpSession session, Model model) {
        List<CartService.CartLine> lines = cartService.getLines(session);
        model.addAttribute("lines", lines);
        model.addAttribute("total", cartService.getTotal(lines));
        return "cart/cart";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long bookId,
                      @RequestParam(defaultValue = "1") int quantity,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        cartService.addItem(session, bookId, quantity);
        redirectAttributes.addFlashAttribute("flash", "Added to cart");
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long bookId,
                         @RequestParam int quantity,
                         HttpSession session) {
        cartService.updateItem(session, bookId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long bookId, HttpSession session) {
        cartService.removeItem(session, bookId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clear(HttpSession session) {
        cartService.clear(session);
        return "redirect:/cart";
    }
}
