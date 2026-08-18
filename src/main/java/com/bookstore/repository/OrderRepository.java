package com.bookstore.repository;

import com.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            select distinct o from Order o
            left join fetch o.items i
            left join fetch i.book
            where o.id = :id
            """)
    Optional<Order> findWithItemsById(@Param("id") Long id);

    @Query("""
            select distinct o from Order o
            left join fetch o.items i
            left join fetch i.book
            where lower(o.customerEmail) = lower(:email)
            order by o.orderDate desc
            """)
    List<Order> findByCustomerEmailWithItems(@Param("email") String email);
}
