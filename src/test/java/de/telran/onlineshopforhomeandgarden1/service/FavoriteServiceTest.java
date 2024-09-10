package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.response.FavoriteResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.entity.Favorite;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.FavoriteMapper;
import de.telran.onlineshopforhomeandgarden1.repository.FavoriteRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import de.telran.onlineshopforhomeandgarden1.security.JwtAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteServiceTest {

    private static FavoriteService favoriteService;
    private static FavoriteRepository favoriteRepository;
    private static FavoriteMapper favoriteMapper;
    private static AuthService authService;

    @BeforeEach
    public void init() {
        favoriteRepository = Mockito.mock(FavoriteRepository.class);
        favoriteMapper = Mappers.getMapper(FavoriteMapper.class);
        authService = Mockito.mock(AuthService.class);
        favoriteService = new FavoriteService(favoriteRepository, favoriteMapper, authService);
    }

    @Test
    public void getFavoritesSuccessful() {
        Favorite first = new Favorite();
        Long favoriteIdFirst = 1L;
        first.setId(favoriteIdFirst);
        Category category = new Category();
        first.setProduct(new Product(favoriteIdFirst, "Test name1", "Test description1", BigDecimal.valueOf(5.6),
                category, "Test image1", BigDecimal.valueOf(4.5), Instant.now(), Instant.now()));


        Favorite second = new Favorite();
        Long favoriteIdSecond = 2L;
        second.setId(favoriteIdSecond);
        second.setProduct(new Product(favoriteIdSecond, "Test name2", "Test description2", BigDecimal.valueOf(6.6),
                category, "Test image2", BigDecimal.valueOf(5.5), Instant.now(), Instant.now()));

        Set<Favorite> favorites = Set.of(first, second);

        JwtAuthentication mockAuthInfo = Mockito.mock(JwtAuthentication.class);
        Mockito.when(mockAuthInfo.isAuthenticated()).thenReturn(true);
        Mockito.when(mockAuthInfo.getLogin()).thenReturn("testuser@example.com");
        Mockito.when(authService.getAuthInfo()).thenReturn(mockAuthInfo);

        Mockito.when(favoriteRepository.findFavoriteByUserEmail("testuser@example.com")).thenReturn(favorites);

        Set<FavoriteResponseDto> result = favoriteService.getFavorites();

        Mockito.verify(favoriteRepository).findFavoriteByUserEmail("testuser@example.com");

        assertNotNull(result);
        assertEquals(2, result.size());

    }

    @Test
    public void getFavoritesUnsuccessful() {
        JwtAuthentication mockAuthInfo = Mockito.mock(JwtAuthentication.class);
        Mockito.when(mockAuthInfo.isAuthenticated()).thenReturn(false);
        Mockito.when(mockAuthInfo.getLogin()).thenReturn("testuser@example.com");
        Mockito.when(authService.getAuthInfo()).thenReturn(mockAuthInfo);

        Set<Favorite> favorites = Collections.emptySet();
        Mockito.when(favoriteRepository.findFavoriteByUserEmail("testuser@example.com")).thenReturn(favorites);
        Set<FavoriteResponseDto> result = favoriteService.getFavorites();
        assertEquals(favorites.size(), result.size());
    }

}