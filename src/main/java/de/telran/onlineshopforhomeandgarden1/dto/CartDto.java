package de.telran.onlineshopforhomeandgarden1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;

    private User user;
}
