package com.example.techtask.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.techtask.model.User;
import com.example.techtask.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
class UserServiceImpl implements UserService {

    private final int PRODUCTS_DELIVERED_YEAR = 2003;
    private final int ORDERS_PAID_YEAR = 2010;
    
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public User findUser() {
        try {
            String jpql = """
                    SELECT u
                    FROM User u
                    JOIN u.orders o
                    WHERE YEAR(o.createdAt) = :year
                    AND o.orderStatus = 'DELIVERED'
                    GROUP BY u.id
                    ORDER BY SUM(o.price * o.quantity) DESC
                    """;

            return entityManager
                    .createQuery(jpql, User.class)
                    .setParameter("year", PRODUCTS_DELIVERED_YEAR)
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
    public List<User> findUsers() {
        try {
            String jpql = """
                    SELECT u
                    FROM User u
                    JOIN u.orders o
                    WHERE YEAR(o.createdAt) = :year
                    AND o.orderStatus = 'PAID'
                    GROUP BY u.id
                    """;

           return entityManager
                    .createQuery(jpql, User.class)
                    .setParameter("year", ORDERS_PAID_YEAR)
                    .getResultList();
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
