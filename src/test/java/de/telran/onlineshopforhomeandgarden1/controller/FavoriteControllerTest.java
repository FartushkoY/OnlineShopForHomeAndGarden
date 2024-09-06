package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.response.FavoriteResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.UserResponseDto;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FavoriteController.class)
class FavoriteControllerTest {

    @MockBean
    private FavoriteService service;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void getFavorites() throws Exception {

        Set<FavoriteResponseDto> favorites = new LinkedHashSet<>();
        favorites.add(new FavoriteResponseDto(new UserResponseDto(1L,"David", "+491601601601", "david.mueller@gmail.com", Role.CUSTOMER ),new ProductResponseDto(1L, "Product 1", "Description 1", "Image 1")));
        favorites.add(new FavoriteResponseDto(new UserResponseDto(1L,"David", "+491601601601", "david.mueller@gmail.com", Role.CUSTOMER),new ProductResponseDto(2L, "Product 2", "Description 2", "Image 2")));

        Mockito.when(service.getFavorites()).thenReturn(favorites);

        mockMvc.perform(get("/favorites").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(favorites.size()));
        Mockito.verify(service).getFavorites();

    }
}
