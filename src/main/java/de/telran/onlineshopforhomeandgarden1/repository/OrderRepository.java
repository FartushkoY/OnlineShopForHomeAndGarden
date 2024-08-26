package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Set<Order> findOrdersByUserId(Long userId);

    @Query(("SELECT o FROM Order o WHERE o.status IN ('PENDING', 'PAID', 'ON_THE_WAY')"))
    Set<Order> findOrdersRequiringStatusUpdate();





}
