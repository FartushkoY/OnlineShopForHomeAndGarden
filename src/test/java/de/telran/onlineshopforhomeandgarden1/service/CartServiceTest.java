package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.*;
import de.telran.onlineshopforhomeandgarden1.exception.IllegalOperationInCartException;
import de.telran.onlineshopforhomeandgarden1.mapper.CartItemMapper;
import de.telran.onlineshopforhomeandgarden1.mapper.CartMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartItemRepository;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import de.telran.onlineshopforhomeandgarden1.security.JwtAuthentication;
import jakarta.persistence.EntityNotFoundException;
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
        Set<Cart> mockCarts = Set.of(cart);

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
        CartRequestDto cartRequestDto = service.addCartItem(cartItemMapper.entityToRequestDto(cartItem));

        Mockito.when(cartMapper.entityToRequestDto(cart)).thenReturn(cartRequestDto);

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
    void updateCartItemsInCartTest() {
        Product product1 = new Product();
        product1.setId(1L);

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(1);

        Cart cart1 = new Cart();
        cart1.setId(1L);
        Set<CartItem> cartItems1 = cart1.getCartItems();
        cartItems1.add(cartItem1);

        cartItem1.setCart(cart1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setProduct(product1);
        cartItem2.setQuantity(2);

        Long cartItemId = 1L;
        Integer newQuantity = 5;

        Mockito.when(cartRepository.findByUserEmail(mockAuthInfo.getLogin())).thenReturn(cart1);

        Mockito.when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem1));
        Mockito.when(cartItemRepository.save(cartItem1)).thenReturn(cartItem1);

        CartItemResponseDto cartItemResponseDto = service.updateCartItemInCart(cartItemId, newQuantity);

        Mockito.verify(cartItemRepository).findById(cartItemId);
        Mockito.verify(cartItemRepository).save(any());
        assertEquals(cartItemResponseDto.getQuantity(), 5);
    }


    @Test
    void updateCartItemsInCartNotFoundTest() {
        Product product1 = new Product();
        product1.setId(1L);

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(1);

        Long cartItemId = 111L;

        Mockito.doThrow(new EntityNotFoundException()).when(cartItemRepository).findById(cartItemId);
        Mockito.verify(cartItemRepository, Mockito.never()).save(cartItem1);
    }


    @Test
    void updateCartItemsInCartIllegalTest() {
        Product product1 = new Product();
        product1.setId(1L);

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(1);

        Cart cart = new Cart();
        cart.setId(1L);
        Set<CartItem> cartItems1 = cart.getCartItems();
        cartItems1.add(cartItem1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setProduct(product1);
        cartItem2.setQuantity(2);

        Long cartItemId = 2L;
        Integer newQuantity = 5;

        Mockito.when(cartRepository.findByUserEmail(mockAuthInfo.getLogin())).thenReturn(cart);
        Mockito.doThrow(new IllegalOperationInCartException("CartItem with id = " + cartItemId + " not in cart with id = " + cart.getId())).when(cartItemRepository).findById(cartItemId);

        assertThrows(IllegalOperationInCartException.class, () -> service.updateCartItemInCart(cartItemId, newQuantity));

        Mockito.verify(cartItemRepository).findById(cartItemId);
        Mockito.verify(cartItemRepository, Mockito.never()).save(any());
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