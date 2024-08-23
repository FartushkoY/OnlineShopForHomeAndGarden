package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.response.FavoriteResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Favorite;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.mapper.FavoriteMapper;
import de.telran.onlineshopforhomeandgarden1.repository.FavoriteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FavoriteService {

    private static final Logger logger = LogManager.getLogger(FavoriteService.class);
    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;

    public FavoriteService(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
    }


    public Set<FavoriteResponseDto> getFavorites() {
      Set<Favorite> favorites = favoriteRepository.findFavoriteByUserId(this.getAutheticateUserForFavorites().getId());
        logger.info("Found {} favorites", favorites.size());
        return favoriteMapper.entityListToDto(favorites);
    }

    private User getAutheticateUserForFavorites() {
        User user = new User();
        user.setId(2l);
        return user;
    }
}
