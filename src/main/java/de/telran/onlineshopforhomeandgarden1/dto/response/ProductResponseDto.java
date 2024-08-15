package de.telran.onlineshopforhomeandgarden1.dto.response;

import de.telran.onlineshopforhomeandgarden1.entity.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {


    private Long id;

    @NotNull(message = "{validation.product.name}")
    @Length(max = 90, message = "{validation.product.name}")
    private String name;

    @NotNull(message = "{validation.product.description}")
    @Length(max = 3000, message = "{validation.product.description}")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "{validation.product.description}")
    @Digits(integer = 5, fraction = 2, message = "{validation.product.price}")
    private BigDecimal price;

    private String imageUrl;

}
