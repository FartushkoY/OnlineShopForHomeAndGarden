package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.UserDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto userDto) {
        try {
            UserDto result = service.saveUser(userDto);
            log.info("User with name = {}, email = {}, phoneNumber = {} and passwordHash = {} created",
                    result.getName(), result.getEmail(), result.getPhoneNumber(), result.getPasswordHash());
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("User cannot be saved", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId,
                                              @RequestBody @Valid UserDto userDto) {
        try {
            Optional<UserDto> user = service.updateUser(userId, userDto.getName(), userDto.getPhoneNumber());
            UserDto result = user.get();
            log.info("User with id = {} {}", userId, user.isPresent() ? "updated" : "created");
            log.debug("User with id = {} {}, name = {} and phone number = {}", userId, userDto.getName(), userDto.getPhoneNumber());
            return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("User cannot be updated", e);
            return new ResponseEntity<>((HttpStatus.BAD_REQUEST));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        Optional<User> optional = service.removeUser(userId);
        if (optional.isPresent()) {
            log.info("User with id= {} deleted", userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("User cannot be deleted");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
