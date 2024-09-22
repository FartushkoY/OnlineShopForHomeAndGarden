package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import de.telran.onlineshopforhomeandgarden1.mapper.CartItemMapper;
import de.telran.onlineshopforhomeandgarden1.mapper.CartMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartItemRepository;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {
    private static final Logger logger = LogManager.getLogger(CartService.class);
    private final CartRepository repository;
    private final CartMapper mapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final AuthService authService;


    @Autowired
    public CartService(CartRepository repository, CartMapper mapper, CartItemRepository cartItemRepository, CartItemMapper cartItemMapper, AuthService authService) {
        this.repository = repository;
        this.mapper = mapper;
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
        this.authService = authService;
    }

    public CartResponseDto getCartItems() {
        Cart cart = repository.findByUserEmail(authService.getAuthInfo().getLogin());
        logger.debug("Cart contents for user {}: {}", authService.getAuthInfo().getLogin(), cart.getCartItems());
        return mapper.entityToResponseDto(cart);
    }

   @Transactional
    public CartResponseDto addCartItem(CartItemRequestDto cartItemRequestDto) {
        Cart cart = repository.findByUserEmail(authService.getAuthInfo().getLogin());

        Set<CartItem> cartItems = cart.getCartItems();
        CartItem newCartItem = cartItemMapper.requestDtoToEntity(cartItemRequestDto);

        CartItem itemExists = cartItems.stream().filter(cartItem -> cartItem.getProduct().getId()
                .equals(newCartItem.getProduct().getId())).findFirst().orElse(null);

        if (itemExists != null) {
            itemExists.setQuantity(itemExists.getQuantity() + newCartItem.getQuantity());
            logger.debug("Updated quantity of existing product in cart for productId = {}", newCartItem.getProduct().getId());
        } else {
            newCartItem.setCart(cart);
            cartItemRepository.save(newCartItem);
            cartItems.add(newCartItem);
            logger.debug("Added new product to cart for productId = {}", newCartItem.getProduct().getId());
        }

        Cart saved = repository.save(cart);
        logger.debug("Cart with id = {} added product = {} and quantity = {}.", saved.getId(), newCartItem.getProduct(), newCartItem.getQuantity());
        return mapper.entityToResponseDto(saved);
    }

    public CartResponseDto updateCartItemInCart(CartItemRequestDto cartItemRequestDto) {
        Cart cart = repository.findByUserEmail(authService.getAuthInfo().getLogin());
        Set<CartItem> cartItems = cart.getCartItems();
        CartItem newCartItem = cartItemMapper.requestDtoToEntity(cartItemRequestDto);
        CartItem itemExists = cartItems.stream().filter(cartItem -> cartItem.getProduct().getId().equals(newCartItem.getProduct().getId())).findFirst()
                .orElse(null);
        itemExists.setQuantity(newCartItem.getQuantity());

        Cart saved = repository.save(cart);
        logger.debug("Cart with id = {} updated product = {} and quantity = {}.",
                saved.getId(),
                saved.getCartItems().iterator().next().getProduct(),
                saved.getCartItems().iterator().next().getQuantity());
        return mapper.entityToResponseDto(saved);

    }

    public void deleteCartItemsInCart() {
        Cart cart = repository.findByUserEmail(authService.getAuthInfo().getLogin());
        Set<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAllByCart(cart);
            logger.debug("CartItem deleted successfully.");
        }
    }

}
