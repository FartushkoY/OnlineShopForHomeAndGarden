package de.telran.onlineshopforhomeandgarden1.controller;


import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.math.BigDecimal;
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
    public ResponseEntity<ProductWithDiscountPriceResponseDto> getProductById(@PathVariable Long id) {
        Optional<ProductWithDiscountPriceResponseDto> productDto = service.getProductById(id);
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
    public ResponseEntity<ProductRequestDto> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        try {
            ProductRequestDto createdProduct = service.addProduct(productRequestDto);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{productId}")
    public ResponseEntity<ProductRequestDto> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductRequestDto product) {
        try {
            ProductRequestDto updatedProduct = service.updateProduct(productId, product);
            return new ResponseEntity<>(updatedProduct, updatedProduct != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/addDiscount/{id}")
    public ResponseEntity<ProductRequestDto> addDiscount(@PathVariable Long id,
                                                         @RequestParam @Min(value = 0, message = "{validation.product.price}")
                                                         BigDecimal discountPrice) {
        try {
            ProductRequestDto updatedProduct = service.addDiscount(id, discountPrice);
            return new ResponseEntity<>(updatedProduct, updatedProduct != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        Optional<Product> deletedProduct = service.deleteProduct(productId);
        if (deletedProduct.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    @GetMapping("/top10")
    public List<ProductWithPriceResponseDto> getTop10MostPurchasedProducts() {
        return service.getTop10MostPurchasedProducts();

    }

    @GetMapping("/top10Canceled")
    public List<ProductWithPriceResponseDto> getTop10FrequentlyCanceledProducts() {
        return service.getTop10FrequentlyCanceledProducts();

    }

    @GetMapping("/productOfTheDay")
    public ResponseEntity<ProductWithDiscountPriceResponseDto> getProductOfTheDay() {
        Optional<ProductWithDiscountPriceResponseDto> productOfTheDay = service.getProductOfTheDay();
        if (productOfTheDay.isPresent()) {
            return new ResponseEntity<>(productOfTheDay.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/pendingMoreThan/{days}")
    public ResponseEntity<List<ProductWithPriceResponseDto>> getPendingProducts(@PathVariable int days) {
        List<ProductWithPriceResponseDto> pendingProducts = service.getPendingProducts(days);
        if (pendingProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(pendingProducts, HttpStatus.OK);
        }
    }

}