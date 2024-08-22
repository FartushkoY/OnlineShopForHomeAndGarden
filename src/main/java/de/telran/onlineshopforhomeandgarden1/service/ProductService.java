package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.ProductDto;
import de.telran.onlineshopforhomeandgarden1.dto.RequestDto.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.ProductMapper;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository repository, ProductMapper productMapper) {
        this.repository = repository;
        this.productMapper = productMapper;
    }


    public Optional<ProductDto> getProductById(Long id) {
        Optional<Product> optional = repository.findById(id);
        return optional.map(productMapper::entityToDto);
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
        return products.map(productMapper::entityToWithDiscountResponseDto);
    }

    public ProductRequestDto addProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.requestDtoToEntity(productRequestDto);
        Product created = repository.save(product);
        return productMapper.entityToRequestDto(created);
    }

    public ProductRequestDto updateProduct(ProductRequestDto product) {
        Optional<Product> optional = repository.findById(product.getId());
        if (optional.isPresent()) {
            Product updated = repository.save(productMapper.requestDtoToEntity(product));
            return productMapper.entityToRequestDto(updated);
        } else {
            return null;
        }
    }
}
