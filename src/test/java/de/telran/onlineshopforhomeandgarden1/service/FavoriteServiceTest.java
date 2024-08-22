package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.response.FavoriteResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.entity.Favorite;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.FavoriteMapper;
import de.telran.onlineshopforhomeandgarden1.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteServiceTest {

    private static FavoriteService favoriteService;
    private static FavoriteRepository favoriteRepository;
    private static FavoriteMapper favoriteMapper;

    @BeforeEach
    public void init(){
        favoriteRepository = Mockito.mock(FavoriteRepository.class);
        favoriteMapper = Mappers.getMapper(FavoriteMapper.class);
        favoriteService = new FavoriteService(favoriteRepository, favoriteMapper);
    }

    @Test
    public void getFavorites() {
        Favorite first = new Favorite();
        Long favoriteIdFirst = 1L;
        first.setId(favoriteIdFirst);
        Category category = new Category();
        first.setProduct(new Product(favoriteIdFirst,"Test name1", "Test description1", BigDecimal.valueOf(5.6),
                                     category, "Test image1", BigDecimal.valueOf(4.5), Instant.now(), Instant.now()));


        Favorite second = new Favorite();
        Long favoriteIdSecond = 2L;
        second.setId(favoriteIdSecond);
        second.setProduct(new Product(favoriteIdSecond,"Test name2", "Test description2", BigDecimal.valueOf(6.6),
                category, "Test image2", BigDecimal.valueOf(5.5), Instant.now(), Instant.now()));

        Set<Favorite> favorites = Set.of(first, second);

        Mockito.when(favoriteRepository.findFavoriteByUserId(2L)).thenReturn(favorites);
        Set<FavoriteResponseDto> result = favoriteService.getFavorites();

        Mockito.verify(favoriteRepository).findFavoriteByUserId(2l);

        assertNotNull(result);
        assertEquals(2, result.size());


    }
}