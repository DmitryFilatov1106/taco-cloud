package ru.fildv.tacocloud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fildv.tacocloud.repository.OrderRepository;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final OrderRepository orderRepo;

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllOrders() {
        orderRepo.deleteAll();
    }
}
