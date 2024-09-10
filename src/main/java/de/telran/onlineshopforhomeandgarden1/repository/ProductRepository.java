package de.telran.onlineshopforhomeandgarden1.repository;


import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where (?2 = false or p.category.id = ?1) and (?3 = false or p.discountPrice is not null) and p.price between ?4 and ?5")
    Page<Product> getAllWithFilters(Long categoryId, Boolean hasCategory, Boolean hasDiscount,
                                    BigDecimal minPriceBigDecimal, BigDecimal maxPriceBigDecimal, Pageable pageable);

    List<Product> findAllByCategory(Category category);


    @Query("SELECT oi.product FROM OrderItem oi JOIN oi.order o WHERE o.status NOT IN (de.telran.onlineshopforhomeandgarden1.enums.Status.CANCELED, de.telran.onlineshopforhomeandgarden1.enums.Status.PENDING) GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC LIMIT 10")
    List<Product> findTop10MostPurchasedProducts();


    @Query("select p, (p.price - p.discountPrice)/p.price as discount from Product p where (p.price - p.discountPrice)/p.price = (select  max((p.price - p.discountPrice)/p.price) from Product p) order by rand() limit 1")
    Optional<Product> findProductOfTheDay();

    @Query("select p from Product p order by rand() limit 1")
    Optional<Product> findRandomProduct();

    @Query("SELECT oi.product FROM OrderItem oi JOIN oi.order o WHERE o.status = 'CANCELED' GROUP BY oi.product ORDER BY COUNT(oi) DESC limit 10")
    List<Product> findTop10FrequentlyCanceledProducts();

    @Query("SELECT oi.product FROM OrderItem oi INNER JOIN oi.order o WHERE o.status ='PENDING' AND o.createdAt <= :calculatedDate GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC")
    List<Product> findPendingProductsMoreThanNDays(@Param("calculatedDate") Instant calculatedDate);


}



