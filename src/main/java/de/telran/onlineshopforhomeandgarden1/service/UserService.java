package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.UserDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import de.telran.onlineshopforhomeandgarden1.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }

    public UserDto saveUser(UserDto userDto) {
        if (userDto.getRole() == null) {
            userDto.setRole(Role.CUSTOMER);
        }
        User user = mapper.dtoToEntity(userDto);
        User saved = userRepository.save(user);
        return mapper.entityToDto(saved);

    }

    public Optional<UserDto> updateUser(Long id, String name, String phone) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setName(name);
            user.setPhoneNumber(phone);
            User updatedUser = userRepository.save(user);
            return Optional.of(mapper.entityToDto(updatedUser));
        } else {
            return Optional.empty();
        }
    }

    public void removeUser(Long id) {
        Optional<Cart> optional = cartRepository.findCartByUserId(id);
        if (optional.isPresent()) {
            cartRepository.deleteById(optional.get().getId());
            userRepository.deleteById(id);
        } else {
            userRepository.deleteById(id);
        }

    }
}
