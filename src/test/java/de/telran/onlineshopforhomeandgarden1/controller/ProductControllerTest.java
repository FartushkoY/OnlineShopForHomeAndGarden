package de.telran.onlineshopforhomeandgarden1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.onlineshopforhomeandgarden1.config.SecurityConfig;
import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import de.telran.onlineshopforhomeandgarden1.security.JwtProvider;
import de.telran.onlineshopforhomeandgarden1.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    @MockBean
    private ProductService service;
    private static ProductRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void getProductByIdIsSuccessful() throws Exception {
        Product product = new Product();
        product.setId(1L);

        ProductWithDiscountPriceResponseDto productWithDiscountPriceResponseDto = new ProductWithDiscountPriceResponseDto();
        productWithDiscountPriceResponseDto.setName("Test NameProduct");
        productWithDiscountPriceResponseDto.setDescription("test description");
        productWithDiscountPriceResponseDto.setImageUrl("test url");
        productWithDiscountPriceResponseDto.setPrice(BigDecimal.valueOf(15));
        productWithDiscountPriceResponseDto.setDiscountPrice(BigDecimal.valueOf(1));
        productWithDiscountPriceResponseDto.setCreatedAt(Instant.parse("2024-09-09T11:19:42.12Z"));

        Mockito.when(service.getProductById(product.getId())).thenReturn(Optional.of(productWithDiscountPriceResponseDto));

        mockMvc.perform(get("/products/{id}", product.getId()).contentType("application/json"))
                .andExpect(status().isOk());
        Mockito.verify(service).getProductById(product.getId());
    }

    @Test
    void getProductByIdNotFound() throws Exception {
        Mockito.when(service.getProductById(15L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/{id}", 15L).contentType("application/json"))
                .andExpect(status().isNotFound());
        Mockito.verify(service).getProductById(15L);
    }

    @Test
    void getProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Long categoryId = 1L;
        Boolean hasDiscount = true;
        Integer minPrice = 1;
        Integer maxPrice = 1200;
        Page<ProductWithDiscountPriceResponseDto> products = new PageImpl<>(List.of());

        Mockito.when(service.getAll(categoryId, hasDiscount, minPrice, maxPrice, pageable))
                .thenReturn(products);

        mockMvc.perform(get("/products").contentType("application/json")
                        .param("categoryId", categoryId.toString())
                        .param("hasDiscount", hasDiscount.toString())
                        .param("minPrice", minPrice.toString())
                        .param("maxPrice", maxPrice.toString()))
                .andDo(print());
        Mockito.verify(service).getAll(categoryId, hasDiscount, minPrice, maxPrice, pageable);
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void addProduct() throws Exception {
        ProductRequestDto product = new ProductRequestDto();
        product.setName("Test NameProduct");
        product.setDescription("Test description");
        product.setPrice(BigDecimal.valueOf(10));
        product.setCategoryId(String.valueOf(2L));
        product.setImageUrl("test url");

        Mockito.when(service.addProduct(any(ProductRequestDto.class))).thenReturn(product);

        mockMvc.perform(post("/products").contentType("application/json")
                        .content(mapper.writeValueAsString(product)))
                .andExpect(status().isCreated());

        Mockito.verify(service).addProduct(any(ProductRequestDto.class));

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void updateProductIsSuccessful() throws Exception {
        ProductRequestDto product = new ProductRequestDto();
        product.setName("Test NameProduct");
        product.setDescription("Test description");
        product.setPrice(BigDecimal.valueOf(10));
        product.setCategoryId(String.valueOf(2L));
        product.setImageUrl("test url");

        Mockito.when(service.updateProduct(any(), any(ProductRequestDto.class))).thenReturn(product);

        mockMvc.perform(put("/products/{productId}", 1L).contentType("application/json")
                .content(mapper.writeValueAsString(product))).andExpect(status().isOk());

        Mockito.verify(service).updateProduct(any(), any(ProductRequestDto.class));
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void updateProductIsNotFound() throws Exception {
        ProductRequestDto product = new ProductRequestDto();
        product.setName("Test NameProduct");
        product.setDescription("Test description");
        product.setPrice(BigDecimal.valueOf(10));
        product.setCategoryId(String.valueOf(2L));
        product.setImageUrl("test url");

        Mockito.when(service.updateProduct(eq(15L), any(ProductRequestDto.class))).thenReturn(null);

        mockMvc.perform(put("/products/{productId}", 15L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());

        Mockito.verify(service).updateProduct(eq(15L), any(ProductRequestDto.class));
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void addDiscountIsSuccessful() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("Test NameProduct");
        productRequestDto.setDescription("Test description");
        productRequestDto.setPrice(BigDecimal.valueOf(10.00));
        productRequestDto.setCategoryId(String.valueOf(2L));
        productRequestDto.setImageUrl("test url");

        BigDecimal newDiscount = BigDecimal.valueOf(7.00);

        Mockito.when(service.addDiscount(eq(1L), eq(newDiscount))).thenReturn(productRequestDto);


        mockMvc.perform(put("/products/addDiscount/{id}", 1L)
                        .param("discountPrice", newDiscount.toString())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isOk());

        Mockito.verify(service).addDiscount(eq(1L), eq(newDiscount));

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void addDiscountNotFound() throws Exception {
        BigDecimal newDiscount = BigDecimal.valueOf(7.00);

        Mockito.when(service.addDiscount(eq(1L), eq(newDiscount))).thenReturn(null);


        mockMvc.perform(put("/products/addDiscount/{id}", 15L)
                        .param("discountPrice", newDiscount.toString())
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        Mockito.verify(service).addDiscount(any(), any());
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void deleteProduct() throws Exception {
        Long productId = 2L;

        mockMvc.perform(delete("/products/{productId}", productId))
                .andDo(print()).andExpect(status().isOk());
        Mockito.verify(service).deleteProduct(eq(productId));
    }

    @Test
    void getTop10MostPurchasedProducts() throws Exception {
        List<ProductWithPriceResponseDto> top10 = new ArrayList<>();
        Mockito.when(service.getTop10MostPurchasedProducts()).thenReturn(top10);

        mockMvc.perform(get("/products/top10").contentType("application/json"))
                .andDo(print());
        Mockito.verify(service).getTop10MostPurchasedProducts();
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void getTop10FrequentlyCanceledProducts() throws Exception {
        List<ProductWithPriceResponseDto> top10Canceled = new ArrayList<>();
        Mockito.when(service.getTop10FrequentlyCanceledProducts()).thenReturn(top10Canceled);

        mockMvc.perform(get("/products/top10Canceled").contentType("application/json"))
                .andDo(print());
        Mockito.verify(service).getTop10FrequentlyCanceledProducts();
    }

    @Test
    void getProductOfTheDay() throws Exception {
        ProductWithDiscountPriceResponseDto product = new ProductWithDiscountPriceResponseDto();
        Mockito.when(service.getProductOfTheDay()).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/productOfTheDay").contentType("application/json"))
                .andDo(print()).andExpect(status().isOk());
        Mockito.verify(service).getProductOfTheDay();
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void getPendingProductsIsNotFound() throws Exception {
        int days = 5;
        List<ProductWithPriceResponseDto> pendingProducts = new ArrayList<>();
        Mockito.when(service.getPendingProducts(days)).thenReturn(pendingProducts);

        mockMvc.perform(get("/products/pendingMoreThan/{days}", days).contentType("application/json"))
                .andDo(print()).andExpect(status().isNotFound());
        Mockito.verify(service).getPendingProducts(days);

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void getPendingProductsIsSuccessful() throws Exception {
        int days = 5;

        ProductWithPriceResponseDto productWithPriceResponseDto = new ProductWithPriceResponseDto();
        productWithPriceResponseDto.setId(2L);
        productWithPriceResponseDto.setName("ProductName");
        productWithPriceResponseDto.setPrice(BigDecimal.valueOf(2.00));

        List<ProductWithPriceResponseDto> pendingProducts = new ArrayList<>();
        pendingProducts.add(productWithPriceResponseDto);

        Mockito.when(service.getPendingProducts(days)).thenReturn(pendingProducts);

        mockMvc.perform(get("/products/pendingMoreThan/{days}", days).contentType("application/json"))
                .andDo(print()).andExpect(status().isOk());
        Mockito.verify(service).getPendingProducts(days);

    }
}