package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.*;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.exception.CannotDeleteProductException;
import de.telran.onlineshopforhomeandgarden1.mapper.ProductMapper;
import de.telran.onlineshopforhomeandgarden1.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


public class ProductServiceTest {

    private static ProductService productService;
    private static ProductRepository repository;
    private static CategoryRepository categoryRepository;
    private static FavoriteRepository favoriteRepository;
    private static ProductMapper productMapper;

    @BeforeEach
    public void init() {
        repository = Mockito.mock(ProductRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        favoriteRepository = Mockito.mock(FavoriteRepository.class);
        productMapper = Mappers.getMapper(ProductMapper.class);
        productService = new ProductService(repository, categoryRepository, productMapper);
    }

    @Test
    public void getProductByIdTest() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(product));
        ProductWithDiscountPriceResponseDto result = productService.getProductById(1L).get();

        Mockito.verify(repository).findById(1L);
        assertEquals(product.getName(), result.getName());
    }

    @Test
    public void getProductByIdIncorrectTest() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(repository.findById(111L)).thenReturn(Optional.empty());
        Optional<ProductWithDiscountPriceResponseDto> optional = productService.getProductById(111L);

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

        Mockito.when(repository.getAllWithFilters(categoryId, false, false, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), pageable))
                .thenReturn(products);
        productService.getAll(categoryId, hasDiscount, minPrice, maxPrice, pageable);
        Mockito.verify(repository).getAllWithFilters(categoryId, false, false, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), pageable);

    }

    @Test
    public void addProductTest() {
        Category category = new Category();
        category.setId(1L);
        Product product = new Product();
        product.setName("Test name");
        product.setDescription("Test description");
        product.setPrice(BigDecimal.valueOf(10.4));
        product.setCategory(category);
        product.setImageUrl("https://raw.githubusercontent.com/tel-ran-de");
        product.setDiscountPrice(null);

        Mockito.when(repository.save(product)).thenReturn(product);
        ProductRequestDto savedProduct = productService.addProduct(productMapper.entityToRequestDto(product));

        Mockito.verify(repository).save(eq(product));
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @Test
    public void updateProductTest() {
        Category category = new Category();
        category.setId(4L);

        Category newCategory = new Category();
        newCategory.setId(2L);

        Product oldProduct = new Product();
        oldProduct.setId(22L);
        oldProduct.setName("Test name");
        oldProduct.setDescription("Test description");
        oldProduct.setPrice(BigDecimal.valueOf(10.4));
        oldProduct.setCategory(category);
        oldProduct.setImageUrl("https://raw.githubusercontent.com/tel-ran-de");
        oldProduct.setDiscountPrice(null);

        ProductRequestDto updatedProductDto = new ProductRequestDto();
        updatedProductDto.setName("New test name");
        updatedProductDto.setDescription("New test description");
        updatedProductDto.setPrice(BigDecimal.valueOf(10));
        updatedProductDto.setCategoryId("2");
        updatedProductDto.setImageUrl("new image url");

        Product updatedProduct = new Product();
        updatedProduct.setId(22L);
        updatedProduct.setName("New test name");
        updatedProduct.setDescription("New test description");
        updatedProduct.setPrice(BigDecimal.valueOf(10));
        updatedProduct.setCategory(newCategory);
        updatedProduct.setImageUrl("new image url");
        updatedProduct.setDiscountPrice(null);

        Long productId = 22L;

        Mockito.when(repository.findById(updatedProduct.getId())).thenReturn(Optional.of(oldProduct));
        Mockito.when(categoryRepository.findById(Long.valueOf(updatedProductDto.getCategoryId()))).thenReturn(Optional.of(newCategory));
        Mockito.when(repository.save(updatedProduct)).thenReturn(updatedProduct);
        productService.updateProduct(productId, updatedProductDto);
        Mockito.verify(repository).save(Mockito.eq(updatedProduct));
    }

    @Test
    public void updateProductNotFoundTest() {
        Product updatedProduct = new Product();
        updatedProduct.setId(555L);

        Long productId = 22L;

        Mockito.when(repository.findById(productId)).thenReturn(Optional.empty());
        ProductRequestDto result = productService.updateProduct(productId, productMapper.entityToRequestDto(updatedProduct));
        assertNull(result);
    }

    @Test
    void addDiscountTest() {
        Product updatedProduct = new Product();
        updatedProduct.setId(22L);
        updatedProduct.setName("New test name");
        updatedProduct.setDescription("New test description");
        updatedProduct.setPrice(BigDecimal.valueOf(10));
        updatedProduct.setImageUrl("https://raw.githubusercontent.com/tel-ran-de");
        updatedProduct.setDiscountPrice(null);

        BigDecimal newDiscount = BigDecimal.valueOf(7.00);

        Mockito.when(repository.findById(updatedProduct.getId())).thenReturn(Optional.of(updatedProduct));
        Mockito.when(repository.save(updatedProduct)).thenReturn(updatedProduct);
        productService.addDiscount(22L, newDiscount);

        Mockito.verify(repository).save(updatedProduct);
        assertEquals(updatedProduct.getDiscountPrice(), newDiscount);
    }

    @Test
    public void addDiscountNotFoundTest() {
        Product product = new Product();
        product.setId(22L);

        Long id = 555L;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        ProductRequestDto result = productService.addDiscount(id, BigDecimal.valueOf(7.00));
        assertNull(result);
    }


    @Test
    public void deleteProductTest() {
        Product product = new Product();
        product.setId(4L);

        Mockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        productService.deleteProduct(product.getId());
        Mockito.verify(repository).deleteById(product.getId());
    }

    @Test
    public void deleteProductCannotBeDeletedTest() {
        Product product = new Product();
        product.setId(8L);

        Favorite favorite = new Favorite();
        favorite.setId(1L);
        favorite.setProduct(product);

        Mockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite));

        assertThrows(CannotDeleteProductException.class, () -> productService.deleteProduct(8L));

        Mockito.verify(repository, Mockito.never()).deleteById(product.getId());
        Mockito.verify(repository).findById(8L);
    }


    @Test
    public void deleteProductNotFoundTest() {
        Product product = new Product();
        product.setId(4L);

        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(10L));
        Mockito.verify(repository, Mockito.never()).deleteById(product.getId());
    }

    @Test
   public void getTop10MostPurchasedProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Product product5 = new Product();
        Product product6 = new Product();
        Product product7 = new Product();
        Product product8 = new Product();
        Product product9 = new Product();
        Product product10 = new Product();
        List<Product> productList = Arrays.asList( product1, product2, product3, product4, product5, product6, product7, product8, product9, product10 );

        Mockito.when(repository.findTop10MostPurchasedProducts()).thenReturn(productList);
        List<ProductWithPriceResponseDto> products = productService.getTop10MostPurchasedProducts();
        assertEquals(products.size(), 10);
        Mockito.verify(repository).findTop10MostPurchasedProducts();

    }

    @Test
    public void getTop10FrequentlyCanceledProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Product product5 = new Product();
        Product product6 = new Product();
        Product product7 = new Product();
        Product product8 = new Product();
        Product product9 = new Product();
        Product product10 = new Product();
        List<Product> productList = Arrays.asList( product1, product2, product3, product4, product5, product6, product7, product8, product9, product10 );

        Mockito.when(repository.findTop10FrequentlyCanceledProducts()).thenReturn(productList);
        List<ProductWithPriceResponseDto> products = productService.getTop10FrequentlyCanceledProducts();
        assertEquals(products.size(), 10);
        Mockito.verify(repository).findTop10FrequentlyCanceledProducts();

    }

    @Test
    public void getProductOfTheDayTest() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setDiscountPrice(BigDecimal.valueOf(90));

        Product product2 = new Product();
        product1.setId(2L);
        product1.setName("Product2");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setDiscountPrice(BigDecimal.valueOf(50));

        Mockito.when(repository.findProductOfTheDay()).thenReturn(Optional.of(product2));
        Optional<ProductWithDiscountPriceResponseDto> result = productService.getProductOfTheDay();

        Mockito.verify(repository).findProductOfTheDay();
        assertEquals(product2.getName(), result.get().getName());
    }

    @Test
    public void getProductOfTheDayNotDiscountTest() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setDiscountPrice(null);


        Mockito.when(repository.findProductOfTheDay()).thenReturn(Optional.of(product1));
        Optional<ProductWithDiscountPriceResponseDto> result = productService.getProductOfTheDay();

        Mockito.verify(repository).findProductOfTheDay();
        assertEquals(product1.getName(), result.get().getName());
    }


    @Test
    public void getPendingProducts() {
    Instant time = Instant.now();
    time.minus(2, ChronoUnit.DAYS);
    Mockito.when(repository.findPendingProductsMoreThanNDays(eq(time))).thenReturn(List.of());
    productService.getPendingProducts(2);
    Mockito.verify(repository).findPendingProductsMoreThanNDays(any(Instant.class));

    }
}