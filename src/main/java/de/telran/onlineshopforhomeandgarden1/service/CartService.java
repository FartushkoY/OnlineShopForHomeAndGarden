package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import de.telran.onlineshopforhomeandgarden1.mapper.CartItemMapper;
import de.telran.onlineshopforhomeandgarden1.mapper.CartMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartItemRepository;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {
    private static final Logger logger = LogManager.getLogger(CartService.class);
    private final CartRepository repository;
    private final CartMapper mapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductRepository productRepository;


    @Autowired
    public CartService(CartRepository repository, CartMapper mapper, CartItemRepository cartItemRepository, CartItemMapper cartItemMapper, ProductRepository productRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
        this.productRepository = productRepository;
    }

    public Set<CartResponseDto> getCartItems() {
       List<Cart> carts = repository.findAll();
        logger.info("Found {} carts", carts.size());
        return mapper.entityListToResponseDto(carts);
    }

    public CartRequestDto addCartItem(Long id, CartItemRequestDto cartItemRequestDto) {
        Cart cart = repository.findById(id).orElseGet(() -> {
            Cart newCart = new Cart();
            logger.info("Cart with id = {} created.", newCart.getId());
            return repository.save(newCart);
        });

        Set<CartItem> cartItems = cart.getCartItems();
        CartItem cartItem = cartItemMapper.dtoToRequestEntity(cartItemRequestDto);
        cartItems.add(cartItem);

        Cart saved = repository.save(cart);
        logger.info("Cart with id = {} added product = {} and quantity = {}.", saved.getId(), cartItem.getProduct(), cartItem.getQuantity());
        return mapper.entityToRequestDto(saved);

    }

    public CartRequestDto updateCartItemInCart(Long id, CartItemRequestDto cartItemRequestDto) {
        Optional<Cart> cartById = repository.findById(id);
        if (cartById.isPresent()) {
            Cart cart = cartById.get();
            Set<CartItem> cartItems = cart.getCartItems();
            if (!cartItems.isEmpty()) {
                CartItem cartItem = cartItemMapper.dtoToRequestEntity(cartItemRequestDto);
                cartItemRepository.save(cartItem);
                logger.info("CartItem with id = {} updated successfully.", cartItem.getId());
                cartItems.add(cartItem);
                Cart saved = repository.save(cart);
                logger.info("Cart with id = {} updated successfully.", saved.getId());
                return mapper.entityToRequestDto(saved);
            }
        }
        logger.warn("Cart with id = {} cannot be updated.", cartById.get().getId());
        return null;

    }

    public CartRequestDto deleteCartItemInCart(Long id) {
        Optional<Cart> cartById = repository.findById(id);
        Set<CartItem> cartItems = cartById.get().getCartItems();
        if (cartItems != null && !cartItems.isEmpty()) {
            CartItem cartItem = cartItemRepository.deleteByCartId(id);
            repository.save(cartById.get());
            logger.info("Cart with id = {} has not items.", id);
            logger.info("CartItem with id = {} deleted successfully.", cartItem.getId());
        }
        logger.warn("Cart is empty.");
        return null;

    }

}
