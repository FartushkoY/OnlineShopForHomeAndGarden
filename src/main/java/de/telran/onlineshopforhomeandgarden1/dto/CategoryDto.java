package de.telran.onlineshopforhomeandgarden1.dto;

import de.telran.onlineshopforhomeandgarden1.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @NotNull(message = "{validation.category.name}")
    @Length(max = 50, message = "{validation.category.name}")
    private String name;

    private String imageUrl;

    private List<ProductDto> products;
}
