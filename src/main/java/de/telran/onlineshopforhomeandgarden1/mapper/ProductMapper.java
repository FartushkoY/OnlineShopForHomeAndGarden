package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    //    RequestDto
    @Mapping(target = "category.id", source = "categoryId")
    Product requestDtoToEntity(ProductRequestDto productDto);

    @Mapping(target = "categoryId", source = "category.id")
    ProductRequestDto entityToRequestDto(Product product);


    //    Response
    ProductWithDiscountPriceResponseDto entityToWithDiscountResponseDto(Product product);

    List <ProductWithPriceResponseDto> entityListToWithPriceResponseDto(List<Product> products);
}
