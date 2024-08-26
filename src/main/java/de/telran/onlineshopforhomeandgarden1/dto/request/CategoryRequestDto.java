package de.telran.onlineshopforhomeandgarden1.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {

    private String id;

    @NotNull(message = "{validation.category.name}")
    @Length(min = 1, max = 50, message = "{validation.category.name}")
    private String name;

    @NotNull(message = "{validation.category.imageUrl.notNull}")
    @Length(min = 10, max = 255, message = "{validation.category.imageUrl.size}")
    private String imageUrl;

}
