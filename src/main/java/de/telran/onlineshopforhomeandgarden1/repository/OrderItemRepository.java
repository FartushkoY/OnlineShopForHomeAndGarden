package de.telran.onlineshopforhomeandgarden1.repository;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
