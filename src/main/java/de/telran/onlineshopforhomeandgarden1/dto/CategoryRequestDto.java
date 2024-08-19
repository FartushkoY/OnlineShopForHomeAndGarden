package de.telran.onlineshopforhomeandgarden1.dto;

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
    @Length(max = 50, message = "{validation.category.name}")
    private String name;

    private String imageUrl;

}
