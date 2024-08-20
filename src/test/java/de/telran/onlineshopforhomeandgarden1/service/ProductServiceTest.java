package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.ProductDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.ProductMapper;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class ProductServiceTest {

    private static ProductService productService;
    private static ProductRepository repository;
    private static ProductMapper productMapper;

    @BeforeEach
    public void init() {
        repository = Mockito.mock(ProductRepository.class);
        productMapper = Mappers.getMapper(ProductMapper.class);
        productService = new ProductService(repository, productMapper);
    }

    @Test
    public void getProductByIdTest() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(product));
        ProductDto result = productService.getProductById(1L).get();

        Mockito.verify(repository).findById(1L);
        assertEquals(product.getId(), result.getId());
    }

    @Test
    public void getProductByIdIncorrectTest() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(repository.findById(111L)).thenReturn(Optional.empty());
        Optional<ProductDto> optional = productService.getProductById(111L);

        Mockito.verify(repository).findById(111L);
        assertTrue(optional.isEmpty());
    }


    @Test
    public void getAllTest() {
        Product product = new Product();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Long categoryId = null;
        Boolean hasDiscount = null;
        Integer minPrice = null;
        Integer maxPrice = null;
        Page<Product> products = new PageImpl<>(Collections.singletonList(product));
        ProductWithDiscountPriceResponseDto responseDto = new ProductWithDiscountPriceResponseDto();

        Mockito.when(repository.getAllWithFilters(categoryId, false, false, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), pageable))
                .thenReturn(products);
        productService.getAll(categoryId, hasDiscount, minPrice, maxPrice, pageable);
        Mockito.verify(repository).getAllWithFilters(categoryId, false, false, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), pageable);

    }
}