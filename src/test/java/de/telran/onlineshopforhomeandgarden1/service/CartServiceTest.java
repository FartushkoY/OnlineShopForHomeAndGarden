package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.*;
import de.telran.onlineshopforhomeandgarden1.mapper.CartItemMapper;
import de.telran.onlineshopforhomeandgarden1.mapper.CartMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartItemRepository;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import de.telran.onlineshopforhomeandgarden1.security.JwtAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CartServiceTest {
    private static CartService service;
    private static CartRepository cartRepository;
    private static CartMapper cartMapper;
    private static CartItemRepository cartItemRepository;
    private static CartItemMapper cartItemMapper;
    private static AuthService authService;
    private static JwtAuthentication mockAuthInfo;

    @BeforeEach
    public void init() {
        cartRepository = Mockito.mock(CartRepository.class);
        cartMapper = Mappers.getMapper(CartMapper.class);
        cartItemRepository = Mockito.mock(CartItemRepository.class);
        cartItemMapper = Mappers.getMapper(CartItemMapper.class);
        authService = Mockito.mock(AuthService.class);
        service = new CartService(cartRepository, cartMapper, cartItemRepository, cartItemMapper, authService);
        mockAuthInfo = Mockito.mock(JwtAuthentication.class);
        Mockito.when(mockAuthInfo.isAuthenticated()).thenReturn(true);
        Mockito.when(mockAuthInfo.getLogin()).thenReturn("testuser@example.com");
        Mockito.when(authService.getAuthInfo()).thenReturn(mockAuthInfo);
    }

    @Test
    void getCartItemsSuccessTest() {
        Cart cart = new Cart();
        cart.setId(1L);
        User user = new User();
        user.setEmail(mockAuthInfo.getLogin());
        cart.setUser(user);

        Mockito.when(cartRepository.findByUserEmail("testuser@example.com")).thenReturn(cart);
        CartResponseDto result = service.getCartItems();
        Mockito.verify(cartRepository).findByUserEmail("testuser@example.com");

        assertNotNull(result);
    }

    @Test
    void addCartItemSuccessTest() {
        Cart cart = new Cart();
        cart.setId(2L);
        Set<CartItem> cartItems = cart.getCartItems();
        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItems.add(cartItem);
        cartItem = new CartItem();
        product.setId(6L);
        cartItem.setProduct(product);
        cartItem.setQuantity(5);
        cartItems.add(cartItem);

        Mockito.when(cartRepository.findByUserEmail(mockAuthInfo.getLogin())).thenReturn(cart);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        service.addCartItem(cartItemMapper.entityToRequestDto(cartItem));

        assertEquals(cart.getCartItems().size(), 2);
    }

    @Test
    void addCartItemWhenItemExist() {
        Cart cart = new Cart();
        cart.setId(2L);

        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        Set<CartItem> cartItems = new LinkedHashSet<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setProduct(product);
        updatedCartItem.setQuantity(3);
        Mockito.when(cartRepository.findByUserEmail(mockAuthInfo.getLogin())).thenReturn(cart);
        Mockito.when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        CartRequestDto result = service.addCartItem(cartItemMapper.entityToRequestDto(updatedCartItem));
        assertNotNull(result);
        assertEquals(1, cart.getCartItems().size());
        CartItem updatedItem = cart.getCartItems().iterator().next();
        assertEquals(5, updatedItem.getQuantity());
    }

    @Test
    void updateCartItemsInCart() {
        Cart cart = new Cart();
        cart.setId(2L);
        Set<CartItem> cartItems = cart.getCartItems();

        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cartItems.add(cartItem);

        CartItem updateCartItem = new CartItem();
        updateCartItem.setProduct(product);
        updateCartItem.setQuantity(5);

        cartItems.clear();
        cartItems.add(updateCartItem);

        Mockito.when(cartRepository.findByUserEmail(mockAuthInfo.getLogin())).thenReturn(cart);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        CartRequestDto cartRequestDto = service.updateCartItemInCart(cartItemMapper.entityToRequestDto(updateCartItem));

        Mockito.when(cartMapper.entityToRequestDto(cart)).thenReturn(cartRequestDto);

        assertEquals(cart.getCartItems().size(), 1);
        assertNotNull(cartRequestDto);
        CartItem updatedItem = cart.getCartItems().iterator().next();
        assertEquals(updatedItem.getQuantity(), 5);
    }

    @Test
    void deleteCartItemsInCartSuccessfully() {
        Cart cart = new Cart();
        cart.setId(1L);
        Set<CartItem> cartItems = cart.getCartItems();

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItems.add(cartItem);

        Mockito.when(cartRepository.findByUserEmail(mockAuthInfo.getLogin())).thenReturn(cart);
        Mockito.when(cartItemRepository.findAllByCartId(cart.getId())).thenReturn(cartItems);

        service.deleteCartItemsInCart();

        Mockito.verify(cartItemRepository, Mockito.times(1)).deleteAllByCart(cart);
    }

    @Test
    void deleteCartItemsInCartWhenCartIsEmpty() {
        Cart cart = new Cart();
        cart.setId(1L);

        Mockito.when(cartRepository.findByUserEmail(mockAuthInfo.getLogin())).thenReturn(cart);
        Mockito.when(cartItemRepository.findAllByCartId(cart.getId())).thenReturn(Collections.EMPTY_SET);

       service.deleteCartItemsInCart();

        Mockito.verify(cartItemRepository, Mockito.never()).deleteAllByCart(cart);
    }

}