package de.telran.onlineshopforhomeandgarden1.controller;


import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.exception.CannotDeleteProductException;
import de.telran.onlineshopforhomeandgarden1.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductService.class);
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
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<ProductRequestDto> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        ProductRequestDto createdProduct = service.addProduct(productRequestDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }


    @PutMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<ProductRequestDto> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductRequestDto product) {
        ProductRequestDto updatedProduct = service.updateProduct(productId, product);
        return new ResponseEntity<>(updatedProduct, updatedProduct != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


    @PutMapping("/addDiscount/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<ProductRequestDto> addDiscount(@PathVariable Long id,
                                                         @RequestParam @Min(value = 0, message = "{validation.product.price}")
                                                         BigDecimal discountPrice) {
        ProductRequestDto updatedProduct = service.addDiscount(id, discountPrice);
        return new ResponseEntity<>(updatedProduct, updatedProduct != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ExceptionHandler(CannotDeleteProductException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ResponseEntity<String> handleCannotDeleteProductException(CannotDeleteProductException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }


    @GetMapping("/top10")
    public List<ProductWithPriceResponseDto> getTop10MostPurchasedProducts() {
        return service.getTop10MostPurchasedProducts();
    }


    @GetMapping("/top10Canceled")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public List<ProductWithPriceResponseDto> getTop10FrequentlyCanceledProducts() {
        return service.getTop10FrequentlyCanceledProducts();
    }


    @GetMapping("/productOfTheDay")
    public ResponseEntity<ProductWithDiscountPriceResponseDto> getProductOfTheDay() {
        Optional<ProductWithDiscountPriceResponseDto> productOfTheDay = service.getProductOfTheDay();
        return new ResponseEntity<>(productOfTheDay.get(), HttpStatus.OK);
    }


    @GetMapping("/pendingMoreThan/{days}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<ProductWithPriceResponseDto>> getPendingProducts(@PathVariable int days) {
        List<ProductWithPriceResponseDto> pendingProducts = service.getPendingProducts(days);
        if (pendingProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(pendingProducts, HttpStatus.OK);
        }
    }
}