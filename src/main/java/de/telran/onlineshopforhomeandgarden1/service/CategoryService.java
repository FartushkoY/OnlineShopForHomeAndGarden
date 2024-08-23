package de.telran.onlineshopforhomeandgarden1.service;


import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.CategoryMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CategoryRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return categoryMapper.entityListToDto(categories);
    }

    public CategoryRequestDto addCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.dtoToRequestEntity(categoryRequestDto);
        logger.info("Category with id = {} created", category.getId());
        Category newCategory = repository.save(category);
        return categoryMapper.entityToRequestDto(newCategory);
    }

    public CategoryRequestDto updateCategory(CategoryRequestDto categoryRequestDto) {
        Optional<Category> optional = repository.findById(Long.valueOf(categoryRequestDto.getId()));
        if (optional.isPresent()) {
            Category updatedCategory = repository.save(categoryMapper.dtoToRequestEntity(categoryRequestDto));
            return categoryMapper.entityToRequestDto(updatedCategory);
        } else {
            return null;
        }
    }

    public Optional<Category> delete(Long id) {
        Optional<Category> category = repository.findById(id);
        if(category.isEmpty()) {
            return Optional.empty();
        }
        List<Product> products = productRepository.findAllByCategory(category.get());
        products.forEach(p -> p.setCategory(null));
        productRepository.saveAll(products);
        category.ifPresent(repository::delete);
        return category;

    }
}




