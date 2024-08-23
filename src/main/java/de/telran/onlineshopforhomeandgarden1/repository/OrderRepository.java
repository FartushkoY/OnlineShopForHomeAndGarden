package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Set<Order> findOrdersByUserId(Long userId);



}
