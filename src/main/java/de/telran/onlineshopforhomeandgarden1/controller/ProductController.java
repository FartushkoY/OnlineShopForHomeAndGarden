package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.ProductDto;
import de.telran.onlineshopforhomeandgarden1.dto.RequestDto.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductDto> productDto = service.getProductById(id);
        if (productDto.isPresent()) {
            return new ResponseEntity<>(productDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Page<ProductWithDiscountPriceResponseDto> getProducts(@RequestParam(required = false) Long categoryId,
                                                                 @RequestParam(required = false) Boolean hasDiscount,
                                                                 @RequestParam(required = false) Integer minPrice,
                                                                 @RequestParam(required = false) Integer maxPrice,
                                                                 @PageableDefault(size = 10)
                                                                 @SortDefault.SortDefaults({@SortDefault(sort = "name")})
                                                                 Pageable pageable) {
        return service.getAll(categoryId, hasDiscount, minPrice, maxPrice, pageable);
    }

    @PostMapping
    public ResponseEntity<ProductRequestDto> addProduct(@RequestBody ProductRequestDto productRequestDto) {
        try {
            ProductRequestDto createdProduct = service.addProduct(productRequestDto);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping
    public ResponseEntity<ProductRequestDto> updateProduct(@RequestBody @Valid ProductRequestDto product) {
        try {
            ProductRequestDto updatedProduct = service.updateProduct(product);
            return new ResponseEntity<>(updatedProduct, updatedProduct != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
