package de.telran.onlineshopforhomeandgarden1.dto.response;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryResponseDto {

    Long id;

    private String name;

    private String imageUrl;

}

