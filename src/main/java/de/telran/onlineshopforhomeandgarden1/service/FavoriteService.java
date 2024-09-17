package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.response.FavoriteResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Favorite;
import de.telran.onlineshopforhomeandgarden1.mapper.FavoriteMapper;
import de.telran.onlineshopforhomeandgarden1.repository.FavoriteRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class FavoriteService {

    private static final Logger logger = LogManager.getLogger(FavoriteService.class);
    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final AuthService authService;

    public FavoriteService(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper, AuthService authService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
        this.authService = authService;
    }

    public Set<FavoriteResponseDto> getFavorites() {
        Set<Favorite> favorites = favoriteRepository.findFavoriteByUserEmail(authService.getAuthInfo().getLogin());
        logger.debug("Found {} favorites", favorites.size());
        return favoriteMapper.entityListToDto(favorites);
    }
}


