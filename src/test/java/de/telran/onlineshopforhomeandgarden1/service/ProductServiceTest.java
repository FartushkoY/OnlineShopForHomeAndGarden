package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.ProductDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.ProductMapper;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class ProductServiceTest {

    private static ProductService productService;
    private static ProductRepository repository;
    private static ProductMapper productMapper;

    @BeforeAll
    public static void init() {
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
}