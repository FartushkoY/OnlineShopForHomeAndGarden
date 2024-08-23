package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByProductId(Long id);
}
