package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public UserRequestDto saveUser(UserRequestDto userRequestDto) {
        if (userRequestDto.getRole() == null) {
            userRequestDto.setRole(String.valueOf(Role.CUSTOMER));
        }
        User user = mapper.dtoToRequestEntity(userRequestDto);
        User saved = repository.save(user);
        logger.info("User with id = {} created", saved.getId());
        return mapper.entityToRequestDto(saved);

    }

    public Optional<UserRequestDto> updateUser(Long id, UserRequestDto userRequestDto) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = mapper.dtoToRequestEntity(userRequestDto);
            user.setId(id);
            User updatedUser = repository.save(user);
            logger.info("User with id = {} updated", id);
            return Optional.of(mapper.entityToRequestDto(updatedUser));
        } else {
            logger.warn("User with id = {} not found", id);
            return Optional.empty();
        }
    }

    public Optional<User> removeUser(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            repository.deleteById(user.get().getId());
            logger.info("User with id = {} deleted", user.get().getId());
            return user;
        } else {
            logger.warn("User with id = {} not found", id);
            return Optional.empty();
        }
    }

}
