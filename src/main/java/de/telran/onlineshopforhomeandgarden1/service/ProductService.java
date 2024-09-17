package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.*;
import de.telran.onlineshopforhomeandgarden1.exception.CannotDeleteProductException;
import de.telran.onlineshopforhomeandgarden1.mapper.ProductMapper;
import de.telran.onlineshopforhomeandgarden1.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository repository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }


    public Optional<ProductWithDiscountPriceResponseDto> getProductById(Long id) {
        Optional<Product> optional = repository.findById(id);
        return optional.map(productMapper::entityToWithDiscountResponseDto);
    }


    public Page<ProductWithDiscountPriceResponseDto> getAll(Long categoryId, Boolean hasDiscount, Integer minPrice, Integer maxPrice, Pageable pageable) {
        BigDecimal minPriceBigDecimal;
        BigDecimal maxPriceBigDecimal;
        Boolean hasCategory;

        hasCategory = categoryId != null;

        if (hasDiscount == null) {
            hasDiscount = false;
        }
        minPriceBigDecimal = minPrice == null ? BigDecimal.valueOf(0) : BigDecimal.valueOf(minPrice);
        maxPriceBigDecimal = maxPrice == null ? BigDecimal.valueOf(Integer.MAX_VALUE) : BigDecimal.valueOf(maxPrice);

        Page<Product> products = repository.getAllWithFilters(categoryId, hasCategory, hasDiscount, minPriceBigDecimal, maxPriceBigDecimal, pageable);
        logger.debug("Products retrieved from DB: {}", products.getTotalElements());
        return products.map(productMapper::entityToWithDiscountResponseDto);
    }


    public ProductRequestDto addProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.requestDtoToEntity(productRequestDto);
        Product created = repository.save(product);
        logger.debug("Product created with id {}", created.getId());
        return productMapper.entityToRequestDto(created);
    }


    public ProductRequestDto updateProduct(Long productId, ProductRequestDto productDto) {
        Optional<Product> optional = repository.findById(productId);
        if (optional.isPresent()) {
            Product updated = optional.get();
            if (productDto.getName() != null) {
                updated.setName(productDto.getName());
            }
            if (productDto.getDescription() != null) {
                updated.setDescription(productDto.getDescription());
            }
            if (productDto.getPrice() != null) {
                updated.setPrice(productDto.getPrice());
            }
            if (productDto.getCategoryId() != null) {
                updated.setCategory(categoryRepository.getReferenceById(Long.valueOf(productDto.getCategoryId())));
            }
            if (productDto.getImageUrl() != null) {
                updated.setImageUrl(productDto.getImageUrl());
            }
            repository.save(updated);
            logger.debug("Product with id = {} updated", productId);
            return productMapper.entityToRequestDto(updated);
        } else {
            logger.debug("Product with id {} not found", productId);
            return null;
        }
    }


    public ProductRequestDto addDiscount(Long id, BigDecimal discountPrice) {
        Optional<Product> optional = repository.findById(id);
        if (optional.isPresent()) {
            Product updated = optional.get();
            updated.setDiscountPrice(discountPrice);
            updated = repository.save(updated);
            return productMapper.entityToRequestDto(updated);
        } else {
            return null;
        }
    }


    public void deleteProduct(Long id) {
        Optional<Product> optional = Optional.ofNullable(repository.findById(id).orElseThrow(EntityNotFoundException::new));
        if (optional.isPresent()) {
            try {
                optional.get().setCategory(null);
                repository.deleteById(id);
                logger.debug("Product with id = {} deleted", id);
            } catch (DataIntegrityViolationException ex) {
                logger.debug("Product with id = {} cannot be deleted", id);
                throw new CannotDeleteProductException("Product with id = " + id + " cannot be deleted");
            }
        }
    }


    public List<ProductWithPriceResponseDto> getTop10MostPurchasedProducts() {
        List<Product> topTen = repository.findTop10MostPurchasedProducts();
        return productMapper.entityListToWithPriceResponseDto(topTen);
    }


    public List<ProductWithPriceResponseDto> getTop10FrequentlyCanceledProducts() {
        List<Product> topTenCanceled = repository.findTop10FrequentlyCanceledProducts();
        return productMapper.entityListToWithPriceResponseDto(topTenCanceled);
    }


    public Optional<ProductWithDiscountPriceResponseDto> getProductOfTheDay() {
        Optional<Product> optional = repository.findProductOfTheDay();
        if (optional.isPresent()) {
            return optional.map(productMapper::entityToWithDiscountResponseDto);
        } else {
            optional = repository.findRandomProduct();
            return optional.map(productMapper::entityToWithDiscountResponseDto);
        }
    }


    public List<ProductWithPriceResponseDto> getPendingProducts(int days) {
        Instant calculatedDate = Instant.now().minus(days, ChronoUnit.DAYS);
        List<Product> pendingProducts = repository.findPendingProductsMoreThanNDays(calculatedDate);
        return productMapper.entityListToWithPriceResponseDto(pendingProducts);
    }
}

