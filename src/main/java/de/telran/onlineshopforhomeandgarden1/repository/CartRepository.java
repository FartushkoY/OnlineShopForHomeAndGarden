package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    void deleteByUserId(Long userId);
}
