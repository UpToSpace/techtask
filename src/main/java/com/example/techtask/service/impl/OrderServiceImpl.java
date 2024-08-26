package com.example.techtask.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.techtask.model.Order;
import com.example.techtask.model.enumiration.UserStatus;
import com.example.techtask.service.OrderService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
class OrderServiceImpl implements OrderService {

    private final int NUMBER_OF_ITEMS = 1;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Order findOrder() {
        try {
            String jpql = """
                SELECT o
                FROM Order o
                WHERE o.quantity > :quantity
                ORDER BY o.createdAt DESC
                """;

            return entityManager
                    .createQuery(jpql, Order.class)
                    .setParameter("quantity", NUMBER_OF_ITEMS)
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findOrders() {
        try {
            String jpql = """
                SELECT o
                FROM Order o
                JOIN User u
                ON o.userId = u.id
                WHERE u.userStatus = ACTIVE
                ORDER BY o.createdAt
                """;

            return entityManager
                    .createQuery(jpql, Order.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
