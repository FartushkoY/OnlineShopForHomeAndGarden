package de.telran.onlineshopforhomeandgarden1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Long id;

    @NotNull
    @Length(max = 45, message = "{validation.user.name}")
    @Pattern(regexp ="[A-Za-z\\s.'-]{1,45}", message = "validation.user.name")
    private String name;

    @NotNull
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{validation.user.email}")
    @Size(max =45 , message = "{validation.user.email}")
    private String email;

    @NotNull
    @Length(max = 13, message = "{validation.user.phoneNumber}")
    @Pattern(regexp = "[+49\\d]{13}", message = "{validation.user.phoneNumber}")
    private String phoneNumber;

    @org.antlr.v4.runtime.misc.NotNull
    @Length(max = 45, message = "{validation.user.passwordHash}")
    private String passwordHash;

    private String role;

}
