package ru.fildv.tacocloud.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import ru.fildv.tacocloud.component.OrderProperty;
import ru.fildv.tacocloud.model.TacoOrder;
import ru.fildv.tacocloud.model.User;
import ru.fildv.tacocloud.repository.OrderRepository;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {
    private final OrderRepository orderRepo;
    private final OrderProperty orderProperty;

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(final @Valid TacoOrder order,
                               final Errors errors,
                               final SessionStatus sessionStatus,
                               final @AuthenticationPrincipal User user) {
        if (errors.hasErrors()) {
            return "orderForm";
        }

        order.setUser(user);

        orderRepo.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @GetMapping
    public String ordersForUser(
            final @AuthenticationPrincipal User user,
            final Model model) {
        PageRequest page = PageRequest.of(0, orderProperty.getPageSize());

        model.addAttribute(
                "orders",
                orderRepo.findByUserOrderByPlacedAtDesc(user, page));
        return "orderList";
    }
}
