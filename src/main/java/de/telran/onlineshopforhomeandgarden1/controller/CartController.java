package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.UserDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.service.CartService;
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

    @PostMapping("/{cartId}")
    public ResponseEntity<CartRequestDto> addItemToCart(@PathVariable("cartId")Long cartId,
                                                        @RequestBody CartItemRequestDto cartItemRequestDto){
        try{
            CartRequestDto result = service.addCartItem(cartId, cartItemRequestDto);
            log.info("Cart for user = {} with cartItem = {} created", result.getUserId(), result.getItems());
            log.info("CartItem with product = {} and quantity = {} created",
                    cartItemRequestDto.getProductId(), cartItemRequestDto.getQuantity());
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Cart with id = {} has Bad Request", cartId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartRequestDto> updateCartItem(@PathVariable("cartId") Long cartId, @RequestBody CartItemRequestDto cartItemRequestDto){
        try {
            CartRequestDto cart = service.updateCartItemInCart(cartId, cartItemRequestDto);
            log.info("Cart for user = {} with cartItem = {} updated", cartId, cart.getId());
            log.info("CartItem for product = {}, quantity = {} updated",
                    cartItemRequestDto.getProductId(), cartItemRequestDto.getQuantity());
            return new ResponseEntity<>(cart, cart != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Cart with id = {} has Bad Request", cartId);
            return  new ResponseEntity<>((HttpStatus.BAD_REQUEST));
        }
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCartItemInCart(@PathVariable("cartId") Long cartId) {
        try{
            service.deleteCartItemInCart(cartId);
            log.info("CartItem in Cart = {} deleted", cartId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e ){
            log.error("Cart with id = {} not found", cartId);
            return  new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
    }
}
