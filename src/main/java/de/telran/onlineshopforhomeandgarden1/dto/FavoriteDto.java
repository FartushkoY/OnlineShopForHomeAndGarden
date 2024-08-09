package de.telran.onlineshopforhomeandgarden1.dto;

import de.telran.onlineshopforhomeandgarden1.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDto {

    private Long id;

//    private User user;

    private Product product;
}
