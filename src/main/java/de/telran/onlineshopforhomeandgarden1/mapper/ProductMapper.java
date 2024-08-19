package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.ProductDto;
import de.telran.onlineshopforhomeandgarden1.dto.ProductRequestDto.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product dtoToEntity(ProductDto productDto);

    ProductDto entityToDto(Product product);

    List<ProductDto> entityListToDto(List<Product> products);

    //    RequestDto
    Product RequestDtoToEntity(ProductRequestDto productDto);

    ProductRequestDto entityToRequestDto(Product product);

    List<ProductRequestDto> entityListToRequestDto(List<Product> products);

    //    Response
    ProductResponseDto entityToResponseDto(Product product);

    ProductWithDiscountPriceResponseDto entityToWithDiscountResponseDto(Product product);

    List<ProductResponseDto> entityListToResponseDto(List<Product> products);

}
