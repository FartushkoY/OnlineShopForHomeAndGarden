package de.telran.onlineshopforhomeandgarden1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithDiscountPriceResponseDto {

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal discountPrice;

    private String imageUrl;

    private Instant createdAt;

}
