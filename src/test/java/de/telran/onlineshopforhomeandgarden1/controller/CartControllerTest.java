package de.telran.onlineshopforhomeandgarden1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.onlineshopforhomeandgarden1.config.SecurityConfig;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.CartItemMapper;
import de.telran.onlineshopforhomeandgarden1.security.JwtProvider;
import de.telran.onlineshopforhomeandgarden1.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class)
@Import(SecurityConfig.class)
class CartControllerTest {

    @MockBean
    private CartService cartService;

    @MockBean
    private CartItemMapper cartItemMapper;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    void getItemsInCart() throws Exception {
        mockMvc.perform(get("/cart").contentType("application/json"))
                .andExpect(status().isOk());
        verify(cartService).getCartItems();
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    void addItemToCart() throws Exception {
        CartItemRequestDto cartItem = new CartItemRequestDto();
        cartItem.setProductId(String.valueOf(1L));
        cartItem.setQuantity(3);
        CartResponseDto cart = new CartResponseDto();
        cart.setId(2L);
        cart.setId(1L);
        CartItemResponseDto cartItemResponseDto = cartItemMapper.requestDtoToResponseDto(cartItem);

        Set<CartItemResponseDto> items = cart.getCartItems();
        items.add(cartItemResponseDto);

        Mockito.when(cartService.addCartItem(any(CartItemRequestDto.class))).thenReturn(cart);

        mockMvc.perform(post("/cart")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(cart)))
                .andExpect(status().isCreated());

        Mockito.verify(cartService).addCartItem(any(CartItemRequestDto.class));

        assertEquals(cart.getCartItems().size(),1);
        assertNotNull(cartItem);
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    void updateCartItemIsSuccessful() throws Exception {
        ProductResponseDto product = new ProductResponseDto();
        product.setId(1L);

        CartItemRequestDto cartItem1 = new CartItemRequestDto();
        cartItem1.setProductId(String.valueOf(product.getId()));
        cartItem1.setQuantity(1);

        CartRequestDto cart1 = new CartRequestDto();
        cart1.setId(1L);
        Set<CartItemRequestDto> cartItems1 = cart1.getItems();
        cartItems1.add(cartItem1);

        CartItemRequestDto cartItem2 = new CartItemRequestDto();
        cartItem2.setProductId(String.valueOf(product.getId()));
        cartItem2.setQuantity(12);

        Long cartItemId = 1L;

        CartItemResponseDto cartItemResponse = new CartItemResponseDto();
        cartItemResponse.setProduct(product);
        cartItemResponse.setQuantity(cartItem2.getQuantity());

        Mockito.when(cartService.updateCartItemInCart(cartItemId, cartItem2.getQuantity()))
                .thenReturn(cartItemResponse);

        mockMvc.perform(put("/cart")
                        .param("cartItemId", String.valueOf(cartItemId))
                        .param("quantity", String.valueOf(cartItem2.getQuantity()))
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(cartService).updateCartItemInCart(any(Long.class), any(Integer.class));

        assertEquals(1, cart1.getItems().size());
        assertEquals(12, cartItem2.getQuantity());
    }


    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    void deleteCartItemsInCart() throws Exception{
        mockMvc.perform(delete("/cart")).andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(cartService).deleteCartItemsInCart();
    }
}