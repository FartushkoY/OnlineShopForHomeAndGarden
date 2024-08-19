package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    Category dtoToRequestEntity (CategoryRequestDto categoryRequestDto);

    CategoryRequestDto entityToRequestDto (Category category);

    List<Category> entityListToDto(List<Category> categories);
}
