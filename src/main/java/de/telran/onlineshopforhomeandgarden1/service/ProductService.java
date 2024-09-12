package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.*;
import de.telran.onlineshopforhomeandgarden1.mapper.ProductMapper;
import de.telran.onlineshopforhomeandgarden1.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final FavoriteRepository favoriteRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository repository, FavoriteRepository favoriteRepository, CartItemRepository cartItemRepository, OrderItemRepository orderItemRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.repository = repository;
        this.favoriteRepository = favoriteRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderItemRepository = orderItemRepository;
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

        if (categoryId == null) {
            hasCategory = false;
        } else hasCategory = true;

        if (hasDiscount == null) {
            hasDiscount = false;
        }

        if (minPrice == null) {
            minPriceBigDecimal = BigDecimal.valueOf(0);
        } else minPriceBigDecimal = BigDecimal.valueOf(minPrice);

        if (maxPrice == null) {
            maxPriceBigDecimal = BigDecimal.valueOf(Integer.MAX_VALUE);
        } else maxPriceBigDecimal = BigDecimal.valueOf(maxPrice);

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

    @Transactional
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
                updated.setCategory(categoryRepository.findById(Long.valueOf(productDto.getCategoryId())).get());
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

    @Transactional
    public ProductRequestDto addDiscount(Long id, BigDecimal discountPrice) {
        Optional<Product> optional = repository.findById(id);
        if (optional.isPresent()) {
            Product updated = optional.get();
            updated.setDiscountPrice(discountPrice);
            updated = repository.save(updated);
            return  productMapper.entityToRequestDto(updated);
        } else {
            return null;
        }
    }

    @Transactional
    public Optional<Product> deleteProduct(Long id) {
        Optional<Product> optional = repository.findById(id);
        Set<Favorite> favorite = favoriteRepository.findAllByProductId(id);
        Set<OrderItem> orderItem = orderItemRepository.findAllByProductId(id);
        Set<CartItem> cartItem = cartItemRepository.findAllByProductId(id);

        if (optional.isPresent() && favorite.isEmpty() && orderItem.isEmpty() && cartItem.isEmpty()) {
            optional.get().setCategory(null);
            repository.deleteById(id);
            logger.debug("Product with id = {} deleted", id);
            return optional;

        } else if (optional.isPresent() && (!favorite.isEmpty() || !orderItem.isEmpty() || !cartItem.isEmpty())) {
            logger.debug("Product with id {} cannot be deleted", id);
            return optional;
        } else {
            logger.debug("Product with id = {} not found", id);
            return Optional.empty();
        }
    }

    public List<ProductWithPriceResponseDto> getTop10MostPurchasedProducts() {
        List<Product> topTen =  repository.findTop10MostPurchasedProducts();
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

