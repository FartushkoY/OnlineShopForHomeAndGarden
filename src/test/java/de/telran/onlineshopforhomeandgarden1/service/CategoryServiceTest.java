package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.mapper.CategoryMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class CategoryServiceTest {
    private static CategoryService categoryService;
    private static CategoryRepository repository;
    private static CategoryMapper categoryMapper;

    @BeforeAll
    public static void setUp() {
        repository = Mockito.mock(CategoryRepository.class);
        categoryMapper = Mappers.getMapper(CategoryMapper.class);
        categoryService = new CategoryService(repository, categoryMapper);
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
        dbCategory.setImageUrl("Test Image");

        Category updatedCategory = new Category();
        updatedCategory.setId(88L);
        updatedCategory.setName("Test Category1");
        updatedCategory.setImageUrl("Test Image1");

        Mockito.when(repository.findById(88L)).thenReturn(Optional.of(dbCategory));
        Mockito.when(repository.save(updatedCategory)).thenReturn(updatedCategory);
        categoryService.updateCategory(categoryMapper.entityToRequestDto(updatedCategory));
        Mockito.verify(repository).save(Mockito.eq(updatedCategory));
    }

    @Test
    public void updateCategoryNotFound() {
        Category updatedCategory = new Category();
        updatedCategory.setId(88L);
        Mockito.when(repository.findById(88L)).thenReturn(Optional.empty());
        CategoryRequestDto resultCategory = categoryService.updateCategory(categoryMapper.entityToRequestDto(updatedCategory));
        assertNull(resultCategory);
    }

}