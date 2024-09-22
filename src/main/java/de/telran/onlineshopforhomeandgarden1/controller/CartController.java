package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.exception.IllegalOperationInCartException;
import de.telran.onlineshopforhomeandgarden1.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cart Controller", description = "Operations related to customer cart")
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
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Update quantity of an item in the customer's cart")
    public ResponseEntity<CartItemResponseDto> updateCartItem(@RequestParam Long cartItemId, Integer quantity) {

            CartItemResponseDto cartItemInCart = service.updateCartItemInCart(cartItemId, quantity);
            return new ResponseEntity<>(cartItemInCart, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalOperationInCartException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<String> handleIllegalOperationInCartException(IllegalOperationInCartException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }



    @DeleteMapping
    @Operation(summary ="Delete an item in the customer's cart")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> deleteCartItemsInCart() {
        service.deleteCartItemsInCart();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
