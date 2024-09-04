package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import de.telran.onlineshopforhomeandgarden1.entity.Favorite;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.ProductMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartItemRepository;
import de.telran.onlineshopforhomeandgarden1.repository.FavoriteRepository;
import de.telran.onlineshopforhomeandgarden1.repository.OrderItemRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final FavoriteRepository favoriteRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository repository, FavoriteRepository favoriteRepository, CartItemRepository cartItemRepository, OrderItemRepository orderItemRepository, ProductMapper productMapper) {
        this.repository = repository;
        this.favoriteRepository = favoriteRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.productMapper = productMapper;
    }


    public Optional<ProductResponseDto> getProductById(Long id) {
        Optional<Product> optional = repository.findById(id);
        return optional.map(productMapper::entityToResponseDto);
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
       logger.info("Products retrieved from DB: {}", products.getTotalElements());
        return products.map(productMapper::entityToWithDiscountResponseDto);
    }

    public ProductRequestDto addProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.requestDtoToEntity(productRequestDto);
        Product created = repository.save(product);
        logger.info("Product created with id {}", created.getId());
        return productMapper.entityToRequestDto(created);
    }

    public ProductRequestDto updateProduct(ProductRequestDto product) {
        Optional<Product> optional = repository.findById(product.getId());
        if (optional.isPresent()) {
            Product updated = repository.save(productMapper.requestDtoToEntity(product));
            logger.info("Product with id = {} updated", product.getId());
            return productMapper.entityToRequestDto(updated);
        } else {
            logger.info("Product with id {} not found", product.getId());
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
            logger.info("Product with id = {} deleted", id);
            return optional;

        } else if (optional.isPresent() && (!favorite.isEmpty() || !orderItem.isEmpty() || !cartItem.isEmpty())) {
            logger.info("Product with id {} cannot be deleted", id);
            return optional;
        } else {
            logger.info("Product with id = {} not found", id);
            return Optional.empty();
        }
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
}

