package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import de.telran.onlineshopforhomeandgarden1.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder encoder, CartRepository cartRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
        this.cartRepository = cartRepository;
    }

    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional
    public UserRequestDto saveUser(UserRequestDto userRequestDto) {
        User user = mapper.requestDtoToEntity(userRequestDto);
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        if (userRequestDto.getPassword() == null) {
            logger.error("password is null");
            throw new IllegalArgumentException("Password cannot be null");
        }
        user.setPasswordHash(encoder.encode(userRequestDto.getPassword()));

        User saved = repository.save(user);
        Cart cart = new Cart();
        cart.setUser(saved);
        cartRepository.save(cart);

        logger.debug("User with id = {} created", saved.getId());
        return mapper.entityToRequestDto(saved);
    }

    public void save(User user) {
        repository.save(user);
    }

    public Optional<UserRequestDto> updateUser(Long id, UserRequestDto userRequestDto) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User updated = userOptional.get();
            if (userRequestDto.getName() != null) {
                updated.setName(userRequestDto.getName());
            }
            if (userRequestDto.getEmail() != null) {
                updated.setEmail(userRequestDto.getEmail());
            }
            if (userRequestDto.getPhoneNumber() != null) {
                updated.setPhoneNumber(userRequestDto.getPhoneNumber());
            }
            if (userRequestDto.getRole() != null) {
                updated.setRole(Role.valueOf(userRequestDto.getRole()));
            }
            if (userRequestDto.getPassword() != null) {
                updated.setPasswordHash(userRequestDto.getPassword());
            }
            repository.save(updated);
            logger.debug("User with id = {} updated", id);
            return Optional.of(mapper.entityToRequestDto(updated));
        } else {
            logger.debug("User with id = {} not found", id);
            return Optional.empty();
        }
    }

    public Optional<User> removeUser(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            repository.deleteById(user.get().getId());
            logger.debug("User with id = {} deleted", user.get().getId());
            return user;
        } else {
            logger.debug("User with id = {} not found", id);
            return Optional.empty();
        }
    }

}
