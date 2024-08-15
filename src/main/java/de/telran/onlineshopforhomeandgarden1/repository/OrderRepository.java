package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {


}
