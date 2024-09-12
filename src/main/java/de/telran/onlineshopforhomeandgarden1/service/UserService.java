package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public UserRequestDto saveUser(UserRequestDto userRequestDto) {
        User user = mapper.requestDtoToEntity(userRequestDto);
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        User saved = repository.save(user);
        logger.debug("User with id = {} created", saved.getId());
        return mapper.entityToRequestDto(saved);
    }

    public void save(User user) {
        repository.save(user);
    }

    public Optional<UserRequestDto> updateUser(Long id, UserRequestDto userRequestDto) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {

            User user = mapper.requestDtoToEntity(userRequestDto);
            user.setId(id);
            User updatedUser = repository.save(user);
            logger.debug("User with id = {} updated", id);
            return Optional.of(mapper.entityToRequestDto(updatedUser));
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
