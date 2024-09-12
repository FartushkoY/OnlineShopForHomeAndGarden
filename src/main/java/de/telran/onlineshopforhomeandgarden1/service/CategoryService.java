package de.telran.onlineshopforhomeandgarden1.service;


import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.CategoryMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CategoryRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    private final CategoryRepository repository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository repository, CategoryMapper categoryMapper, ProductRepository productRepository) {
        this.repository = repository;
        this.categoryMapper = categoryMapper;
        this.productRepository = productRepository;
    }


    public List<CategoryResponseDto> getAll() {
        List<Category> categories = repository.findAll();
        logger.debug("Fetched {} categories from the repository.", categories.size());
        return categoryMapper.entityListToDto(categories);
    }

    public CategoryRequestDto addCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.dtoToRequestEntity(categoryRequestDto);
        logger.debug("Category with id = {} created", category.getId());
        Category newCategory = repository.save(category);
        return categoryMapper.entityToRequestDto(newCategory);
    }



    public CategoryRequestDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Optional<Category> optional = repository.findById(id);
        if (optional.isPresent()) {
            logger.debug("Category with id = {} found.", categoryRequestDto.getId());
            Category category = optional.get();

            if (categoryRequestDto.getName() != null) {
                category.setName(categoryRequestDto.getName());
            }
            if (categoryRequestDto.getImageUrl() != null) {
                category.setImageUrl(categoryRequestDto.getImageUrl());
            }
            Category updatedCategory = repository.save(category);
            logger.debug("Category with id = {} updated successfully.", updatedCategory.getId());
            return categoryMapper.entityToRequestDto(updatedCategory);
        } else {
            logger.debug("Category with id = {} not found. Update failed.", categoryRequestDto.getId());
            return null;
        }
    }


    @Transactional
    public void delete(Long id) {
        Category category = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<Product> products = productRepository.findAllByCategory(category);
        logger.debug("Found {} products associated with Category id = {}. Dissociating them.", products.size(), id);
        products.forEach(p -> p.setCategory(null));
        productRepository.saveAll(products);
        logger.debug ("Category with id = {} deleted successfully.", id);
        repository.delete(category);

    }
}




