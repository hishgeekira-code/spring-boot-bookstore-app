package com.bookstore.controller;

import com.bookstore.dto.CheckoutForm;
import com.bookstore.entity.Order;
import com.bookstore.service.CartService;
import com.bookstore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        if (cartService.isEmpty(session)) {
            return "redirect:/cart";
        }
        List<CartService.CartLine> lines = cartService.getLines(session);
        model.addAttribute("lines", lines);
        model.addAttribute("total", cartService.getTotal(lines));
        model.addAttribute("checkoutForm", new CheckoutForm());
        return "order/checkout";
    }

    @PostMapping("/checkout")
    public String place(@Valid @ModelAttribute("checkoutForm") CheckoutForm form,
                        BindingResult binding,
                        HttpSession session,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        List<CartService.CartLine> lines = cartService.getLines(session);
        if (lines.isEmpty()) {
            return "redirect:/cart";
        }
        if (binding.hasErrors()) {
            model.addAttribute("lines", lines);
            model.addAttribute("total", cartService.getTotal(lines));
            return "order/checkout";
        }

        try {
            Order order = orderService.placeOrder(form, session);
            return "redirect:/orders/" + order.getId() + "/confirmation";
        } catch (ResponseStatusException e) {
            model.addAttribute("lines", lines);
            model.addAttribute("total", cartService.getTotal(lines));
            model.addAttribute("error", e.getReason());
            return "order/checkout";
        }
    }

    @GetMapping("/{id}/confirmation")
    public String confirmation(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getById(id));
        return "order/confirmation";
    }

    @GetMapping
    public String history(@RequestParam(required = false) String email, Model model) {
        model.addAttribute("email", email == null ? "" : email);
        if (email != null && !email.isBlank()) {
            model.addAttribute("orders", orderService.findForEmail(email.trim()));
        }
        return "order/history";
    }
}
