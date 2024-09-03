package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.mapper.CartItemMapper;
import de.telran.onlineshopforhomeandgarden1.mapper.CartMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartItemRepository;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CartService {
    private static final Logger logger = LogManager.getLogger(CartService.class);
    private final CartRepository repository;
    private final CartMapper mapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;


    @Autowired
    public CartService(CartRepository repository, CartMapper mapper, CartItemRepository cartItemRepository, CartItemMapper cartItemMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public Set<CartResponseDto> getCartItems() {
       Set<Cart> carts = repository.findCartsByUserId(this.getAuthenticatedUser().getId());
        logger.debug("Found {} carts", carts.size());
        return mapper.entityListToResponseDto(carts);
    }

    public CartRequestDto addCartItem(CartItemRequestDto cartItemRequestDto) {
        Cart cart = repository.findCartByUserId(this.getAuthenticatedUser().getId());
        Set<CartItem> cartItems = cart.getCartItems();
        CartItem cartItem = cartItemMapper.dtoToRequestEntity(cartItemRequestDto);
        cartItems.add(cartItem);

        Cart saved = repository.save(cart);
        logger.debug("Cart with id = {} added product = {} and quantity = {}.", saved.getId(), cartItem.getProduct(), cartItem.getQuantity());
        return mapper.entityToRequestDto(saved);

    }

    public CartRequestDto updateCartItemInCart(CartItemRequestDto cartItemRequestDto) {
        Cart cartByUserId = repository.findCartByUserId(this.getAuthenticatedUser().getId());
        if (cartByUserId != null) {
            Set<CartItem> cartItems = cartByUserId.getCartItems();
            if (!cartItems.isEmpty()) {
                CartItem cartItem = cartItemMapper.dtoToRequestEntity(cartItemRequestDto);
                cartItemRepository.save(cartItem);
                logger.debug("CartItem with id = {} updated successfully.", cartItem.getId());
                cartItems.add(cartItem);
                Cart saved = repository.save(cartByUserId);
                logger.debug("Cart with id = {} updated successfully.", saved.getId());
                return mapper.entityToRequestDto(saved);
            }
        }
        logger.debug("Cart for user with id = {} cannot be updated.", cartByUserId.getId());
        return null;

    }

    public CartRequestDto deleteCartItemInCart() {
        Cart cartByUserId = repository.findCartByUserId(this.getAuthenticatedUser().getId());
        Set<CartItem> cartItems = cartByUserId.getCartItems();
        if (cartItems != null && !cartItems.isEmpty()) {
            CartItem cartItem = cartItemRepository.deleteCartItemByUserId(this.getAuthenticatedUser().getId());
            repository.save(cartByUserId);
            logger.debug("Cart for user with id = {} has not items.", this.getAuthenticatedUser().getId());
            logger.debug("CartItem with id = {} deleted successfully.", cartItem.getId());
        }
        logger.debug("Cart is empty.");
        return null;

    }
    private User getAuthenticatedUser() {
        User user = new User();
        user.setId(1l);
        return user;
    }
}
