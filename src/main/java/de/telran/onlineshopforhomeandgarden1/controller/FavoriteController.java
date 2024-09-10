package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.response.FavoriteResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService service;

    @Autowired
    public FavoriteController(FavoriteService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Set<FavoriteResponseDto>> getFavorites() {
        return new ResponseEntity<>(service.getFavorites(), HttpStatus.OK);
    }
}
