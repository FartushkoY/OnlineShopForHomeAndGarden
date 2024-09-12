package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.CategoryMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CategoryRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;


class CategoryServiceTest {
    private static CategoryService categoryService;
    private static CategoryRepository repository;
    private static ProductRepository productRepository;
    private static CategoryMapper categoryMapper;


    @BeforeAll
    public static void setUp() {
        repository = Mockito.mock(CategoryRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        categoryMapper = Mappers.getMapper(CategoryMapper.class);
        categoryService = new CategoryService(repository, categoryMapper, productRepository);
    }

    @Test
    public void getAll() {
        categoryService.getAll();
        Mockito.verify(repository).findAll();
    }

    @Test
    public void addCategory() {
        Category category = new Category();
        category.setId(99L);
        category.setName("Test Category");
        category.setImageUrl("Test Image");

        Mockito.when(repository.save(category)).thenReturn(category);
        CategoryRequestDto resultCategory = categoryService.addCategory(categoryMapper.entityToRequestDto(category));

        Mockito.verify(repository).save(category);
        assertEquals(category.getName(), resultCategory.getName());

    }

    @Test
   public void updateCategoryOk() {
        Category dbCategory = new Category();
        dbCategory.setId(88L);
        dbCategory.setName("Test Category");
        dbCategory.setImageUrl("Test Image-url");

        Category updatedCategory = new Category();
        updatedCategory.setId(88L);
        updatedCategory.setName("New Category");
        updatedCategory.setImageUrl("New Image-url");

        CategoryRequestDto updatedCategoryDto = new CategoryRequestDto();
        updatedCategoryDto.setId("88");
        updatedCategoryDto.setName("New Category");
        updatedCategoryDto.setImageUrl("New Image-url");

        Mockito.when(repository.findById(88L)).thenReturn(Optional.of(dbCategory));
        Mockito.when(repository.save(updatedCategory)).thenReturn(updatedCategory);
        categoryService.updateCategory(88L, updatedCategoryDto);
        Mockito.verify(repository).save(Mockito.eq(updatedCategory));
    }

    @Test
    public void updateCategoryNotFound() {
        Category updatedCategory = new Category();
        updatedCategory.setId(88L);
        Mockito.when(repository.findById(88L)).thenReturn(Optional.empty());
        CategoryRequestDto resultCategory = categoryService.updateCategory(88L, categoryMapper.entityToRequestDto(updatedCategory));
        assertNull(resultCategory);
    }

    @Test
    public void delete() {
        Long id = 66L;
        Category category = new Category();
        category.setId(id);
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findAllByCategory(category)).thenReturn(productList);

        categoryService.delete(id);

        Mockito.verify(repository).findById(id);
        Mockito.verify(productRepository).findAllByCategory(category);
        Mockito.verify(productRepository).saveAll(productList);
        Mockito.verify(repository).delete(Mockito.eq(category));


    }

    @Test
    public void deleteNotFound() {
        Long id = 66L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
         assertThrows(EntityNotFoundException.class, () -> {
            categoryService.delete(id);
        });

    }
}
