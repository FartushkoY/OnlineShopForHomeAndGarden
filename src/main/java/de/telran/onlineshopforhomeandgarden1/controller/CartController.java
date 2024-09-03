package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.CartService;
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
public class CartController {
    private final CartService service;

    @Autowired
    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Set<CartResponseDto>> getItemsInCart() {
        return new ResponseEntity<>(service.getCartItems(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CartRequestDto> addItemToCart(@RequestBody @Valid CartItemRequestDto cartItemRequestDto){
        try{
            CartRequestDto result = service.addCartItem(cartItemRequestDto);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<CartRequestDto> updateCartItem(@RequestBody @Valid CartItemRequestDto cartItemRequestDto){
        try {
            CartRequestDto cart = service.updateCartItemInCart(cartItemRequestDto);
            return new ResponseEntity<>(cart, cart != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return  new ResponseEntity<>((HttpStatus.BAD_REQUEST));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCartItemInCart() {
        try{
            service.deleteCartItemInCart();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e ){
            return  new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
    }
}
