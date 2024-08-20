package de.telran.onlineshopforhomeandgarden1.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponseDto {

    private UserResponseDto user;

    private ProductResponseDto product;
}
