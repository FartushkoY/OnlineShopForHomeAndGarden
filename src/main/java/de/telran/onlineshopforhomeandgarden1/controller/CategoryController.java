package de.telran.onlineshopforhomeandgarden1.controller;


import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<CategoryResponseDto> categoryResponseDtoList = service.getAll();
        return new ResponseEntity<>(categoryResponseDtoList, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<CategoryRequestDto> addCategory(@RequestBody @Valid CategoryRequestDto category) {
            CategoryRequestDto newCategory = service.addCategory(category);
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping ("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<CategoryRequestDto> updateCategory(@PathVariable("categoryId") Long categoryId,
                                                             @RequestBody @Valid CategoryRequestDto category) {
            CategoryRequestDto updatedCategory = service.updateCategory(categoryId, category);
            return new ResponseEntity<>(updatedCategory, updatedCategory != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
            service.delete(categoryId);
            return new ResponseEntity<>(HttpStatus.OK);
    }
}

