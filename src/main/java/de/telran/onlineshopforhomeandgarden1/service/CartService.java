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
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Optional;
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
        Cart cart = repository.findByUserId(this.getAuthenticatedUser().getId());

        if (cart == null) {
            cart = new Cart();
            cart.setUser(this.getAuthenticatedUser());
            cart.setCartItems(new LinkedHashSet<>());
            logger.debug("Creating new cart");
        }
        Set<CartItem> cartItems = cart.getCartItems();

        CartItem newCartItem = cartItemMapper.requestDtoToEntity(cartItemRequestDto);
        boolean itemExists = false;
        for (CartItem existingItem : cartItems) {
            if (existingItem.getProduct().getId().equals(newCartItem.getProduct().getId())) {
                existingItem.setQuantity(existingItem.getQuantity() + newCartItem.getQuantity());
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            newCartItem.setCart(cart);
            cartItems.add(newCartItem);
        }

        Cart saved = repository.save(cart);
        logger.debug("Cart with id = {} added product = {} and quantity = {}.", saved.getId(), newCartItem.getProduct(), newCartItem.getQuantity());
        return mapper.entityToRequestDto(saved);
    }

    public CartRequestDto updateCartItemInCart(CartItemRequestDto cartItemRequestDto) {
        Cart cart = repository.findByUserId(this.getAuthenticatedUser().getId());
        if (cart == null) {
            return null;
        } else {
            Set<CartItem> cartItems = cart.getCartItems();
            CartItem newCartItem = cartItemMapper.requestDtoToEntity(cartItemRequestDto);

            for (CartItem existingItem : cartItems) {
                if (existingItem.getProduct().getId().equals(newCartItem.getProduct().getId())) {
                    existingItem.setQuantity(newCartItem.getQuantity());
                    break;
                }
            }

            Cart saved = repository.save(cart);
            logger.debug("Cart with id = {} updated product = {} and quantity = {}.", saved.getId(), newCartItem.getProduct(), newCartItem.getQuantity());
            return mapper.entityToRequestDto(saved);
        }
    }

    @Transactional
    public Optional<Cart> deleteCartItemInCart() {
        Cart cart = repository.findByUserId(this.getAuthenticatedUser().getId());
        Set<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        if (cartItems != null && !cartItems.isEmpty()) {
            cartItemRepository.deleteAllByCart(cart);
            logger.debug("CartItem deleted successfully.");
            return Optional.of(cart);
        } else {
            logger.debug("Cart is empty.");
            return Optional.empty();
        }
    }

    private User getAuthenticatedUser() {
        User user = new User();
        user.setId(8l);
        return user;
    }
}
