package de.telran.onlineshopforhomeandgarden1.dto.response;

import de.telran.onlineshopforhomeandgarden1.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String name;
    private String phoneNumber;
    private String email;
    private Role role;

}

