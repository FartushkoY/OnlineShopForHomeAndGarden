package de.telran.onlineshopforhomeandgarden1.mapper;


import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;

import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;

import de.telran.onlineshopforhomeandgarden1.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {



    Category dtoToRequestEntity (CategoryRequestDto categoryRequestDto);

    CategoryRequestDto entityToRequestDto (Category category);

    Category dtoToEntity(CategoryResponseDto categoryDto);

    CategoryResponseDto entityToDto(Category category);


    List<CategoryResponseDto> entityListToDto(List<Category> categories);
}
