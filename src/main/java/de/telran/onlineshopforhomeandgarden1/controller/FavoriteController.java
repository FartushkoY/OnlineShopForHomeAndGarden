package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.response.FavoriteResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Favorite Controller", description = "Operations related to customer favorite products")
public class FavoriteController {

    private final FavoriteService service;

    @Autowired
    public FavoriteController(FavoriteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Retrieve all customer favorite products")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Set<FavoriteResponseDto>> getFavorites() {
        return new ResponseEntity<>(service.getFavorites(), HttpStatus.OK);
    }
}
