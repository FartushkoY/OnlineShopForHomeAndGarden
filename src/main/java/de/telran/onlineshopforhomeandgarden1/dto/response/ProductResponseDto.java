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

    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

}
