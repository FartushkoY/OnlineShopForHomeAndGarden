package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@Tag(name = "User Controller", description = "Operations related to user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }


    @PostMapping("/register")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<UserRequestDto> saveUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        UserRequestDto result = service.saveUser(userRequestDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_CUSTOMER')")
    @Operation(summary = "Update profile of the authenticated customer identified by his/her ID")
    public ResponseEntity<UserRequestDto> updateUser(@PathVariable("userId") Long userId, @RequestBody UserRequestDto userRequestDto) {
        Optional<UserRequestDto> user = service.updateUser(userId, userRequestDto);
        if (user.isPresent()) {
            UserRequestDto result = user.get();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_CUSTOMER')")
    @Operation(summary = "Delete the customer identified by his/her ID")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        Optional<User> optional = service.removeUser(userId);
        if (optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
