package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Set<CartItem> findAllByProductId(Long id);
    Set<CartItem> findAllByCartId(Long id);

    @Transactional
    @Modifying
    void deleteAllByCart(Cart cart);
}
