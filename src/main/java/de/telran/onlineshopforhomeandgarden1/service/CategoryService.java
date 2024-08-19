package de.telran.onlineshopforhomeandgarden1.service;


import de.telran.onlineshopforhomeandgarden1.dto.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.mapper.CategoryMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository repository, CategoryMapper categoryMapper) {
        this.repository = repository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryRequestDto addCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.dtoToRequestEntity(categoryRequestDto);
        Category newCategory = repository.save(category);
        return categoryMapper.entityToRequestDto(newCategory);
    }

    public List<CategoryResponseDto> getAll() {
        List<Category> categories = repository.findAll();
        logger.debug("Categories retrieved from DB: {}", ()->categories.stream().map(Category::getName).toList());
        return categoryMapper.entityListToDto(categories);
    }
    }

