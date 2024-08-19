package de.telran.onlineshopforhomeandgarden1.controller;


import de.telran.onlineshopforhomeandgarden1.dto.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CategoryRequestDto> addCategory(@RequestBody CategoryRequestDto category) {
        try {
            CategoryRequestDto newCategory = service.addCategory(category);
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}

