package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.mapper.CategoryMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
}