package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.mapper.CategoryMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;


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
    void getAll() {
        categoryService.getAll();
        Mockito.verify(repository).findAll();
    }

}