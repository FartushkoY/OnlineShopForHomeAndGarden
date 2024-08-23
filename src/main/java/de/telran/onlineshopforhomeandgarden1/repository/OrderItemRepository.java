package de.telran.onlineshopforhomeandgarden1.repository;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByProductId(Long id);
}
