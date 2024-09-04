package de.telran.onlineshopforhomeandgarden1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithPriceResponseDto {

    private Long id;

    private String name;

    private BigDecimal price;


}
