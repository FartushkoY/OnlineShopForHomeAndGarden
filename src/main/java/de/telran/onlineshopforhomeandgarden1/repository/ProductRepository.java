package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where (?2 = false or p.category.id = ?1) and (?3 = false or p.discountPrice is not null) and p.price between ?4 and ?5")
    Page<Product> getAllWithFilters(Long categoryId, Boolean hasCategory, Boolean hasDiscount,
                                    BigDecimal minPriceBigDecimal, BigDecimal maxPriceBigDecimal, Pageable pageable);




}
