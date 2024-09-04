package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;
    

    @Test
    void getAllWithFiltersTest() {

        Page<Product> productPage = repository.getAllWithFilters(1L, true, false, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), Pageable.ofSize(10));
        assertEquals(List.of(repository.findById(1L).get(), repository.findById(2L).get(), repository.findById(3L).get()), productPage.getContent());


        productPage = repository.getAllWithFilters(null, false, false, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), Pageable.unpaged());
        assertEquals(repository.findAll(), productPage.getContent());


        productPage = repository.getAllWithFilters(null, false, true, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), Pageable.ofSize(10));
        assertEquals(List.of(repository.findById(6L).get(), repository.findById(10L).get(), repository.findById(13L).get()), productPage.getContent());


        productPage = repository.getAllWithFilters(null, false, false, BigDecimal.valueOf(20), BigDecimal.valueOf(Integer.MAX_VALUE), Pageable.unpaged());
        assertEquals(List.of(repository.findById(10L).get(), repository.findById(11L).get(),
                repository.findById(12L).get(), repository.findById(13L).get(), repository.findById(14L).get(),
                repository.findById(15L).get()), productPage.getContent());


        productPage = repository.getAllWithFilters(null, false, false, BigDecimal.valueOf(0), BigDecimal.valueOf(3), Pageable.unpaged());
        assertEquals(List.of(repository.findById(1L).get(), repository.findById(2L).get(),
                repository.findById(3L).get()), productPage.getContent());


        productPage = repository.getAllWithFilters(null, false, false, BigDecimal.valueOf(10), BigDecimal.valueOf(13), Pageable.unpaged());
        assertEquals(List.of(repository.findById(4L).get(), repository.findById(5L).get(),
                repository.findById(6L).get()), productPage.getContent());


        productPage = repository.getAllWithFilters(2L, true, true, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), Pageable.unpaged());
        assertEquals(List.of(repository.findById(6L).get()), productPage.getContent());


        productPage = repository.getAllWithFilters(null, false, true, BigDecimal.valueOf(30), BigDecimal.valueOf(35), Pageable.unpaged());
        assertEquals(List.of(repository.findById(10L).get()), productPage.getContent());

    }


   @Test
    void findTop10MostPurchasedProducts(){
       List<Product> products = List.of(
               repository.findById(15L).orElseThrow(), repository.findById(5L).orElseThrow(), repository.findById(7L).orElseThrow(),
               repository.findById(12L).orElseThrow(), repository.findById(3L).orElseThrow(), repository.findById(2L).orElseThrow(),
               repository.findById(1L).orElseThrow(), repository.findById(6L).orElseThrow(), repository.findById(9L).orElseThrow(),
               repository.findById(11L).orElseThrow());
       List<Product> top10MostPurchasedProducts = repository.findTop10MostPurchasedProducts();
       assertEquals(products, top10MostPurchasedProducts);
   }


    @Test
    void findProductOfTheDayTest() {
        List<Optional<Product>> results = List.of(repository.findById(10L), repository.findById(16L), repository.findById(17L));
        Optional<Product> productOfTheDay = repository.findProductOfTheDay();
        assertTrue(results.contains(productOfTheDay));

    }


    @Test
    void findRandomProduct() {
        List<Product> products = repository.findAll();
        Optional<Product> randomProduct = repository.findRandomProduct();
        assertTrue(products.contains(randomProduct.get()));
    }

}