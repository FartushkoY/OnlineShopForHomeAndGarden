package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<CategoryRequestDto> addCategory(@RequestBody CategoryRequestDto category) {
        CategoryRequestDto newCategory = service.addCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }
}
