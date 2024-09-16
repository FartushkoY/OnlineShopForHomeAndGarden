package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    public ResponseEntity<Set<CartResponseDto>> getItemsInCart() {
        return new ResponseEntity<>(service.getCartItems(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Add an item to the customer's cart")
    public ResponseEntity<CartRequestDto> addItemToCart(@RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        CartRequestDto result = service.addCartItem(cartItemRequestDto);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @Operation(summary = "Update quantity of an item in the customer's cart")
    public ResponseEntity<CartRequestDto> updateCartItem(@RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        CartRequestDto cart = service.updateCartItemInCart(cartItemRequestDto);
        if (cart != null) {
            return new ResponseEntity<>(cart, cart != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>((HttpStatus.BAD_REQUEST));
        }
    }

    @DeleteMapping
    @Operation(summary ="Delete an item in the customer's cart")
    public ResponseEntity<?> deleteCartItemInCart() {
        service.deleteCartItemInCart();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
