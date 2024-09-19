package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Validated
@Slf4j
public class CartController {
    private final CartService service;

    @Autowired
    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Retrieve all items in the customer's cart")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartResponseDto> getItemsInCart() {
        return new ResponseEntity<>(service.getCartItems(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Add an item to the customer's cart")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartRequestDto> addItemToCart(@RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        CartRequestDto result = service.addCartItem(cartItemRequestDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update quantity of an item in the customer's cart")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartRequestDto> updateCartItem(@RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        CartRequestDto cart = service.updateCartItemInCart(cartItemRequestDto);
        if (cart != null) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    @Operation(summary ="Delete an item in the customer's cart")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> deleteCartItemsInCart() {
        service.deleteCartItemsInCart();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
