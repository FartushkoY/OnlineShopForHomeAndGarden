package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.UserDto;
import de.telran.onlineshopforhomeandgarden1.service.UserService;
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
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId")Long userId,
                                              @RequestBody UserDto userDto){
        try {
            Optional<UserDto> user = service.updateUser(userId, userDto.getName(), userDto.getPhoneNumber());
            UserDto result = user.get();
            return new ResponseEntity<>(result, result.getName() != null || result.getPhoneNumber() != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return  new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
    }

}
