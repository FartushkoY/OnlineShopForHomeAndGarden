package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.UserDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;


class UserServiceTest {
    private static UserService service;
    private static UserRepository repository;
    private static UserMapper mapper;

    @BeforeEach
    public void init() {
        repository = Mockito.mock(UserRepository.class);
        mapper = Mappers.getMapper(UserMapper.class);
        service = new UserService(repository, mapper);
    }

    @Test
    public void addUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test Name");
        user.setEmail("test@test.eu");
        user.setPhoneNumber("+491715207968");
        user.setRole(Role.CUSTOMER);

        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setName("Old Name");
        oldUser.setEmail("Old_test@test.eu");
        oldUser.setPhoneNumber("+491715207968");

        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(oldUser));
        Mockito.when(repository.save(user)).thenReturn(user);

        UserDto result = service.addUser(mapper.entityToDto(user));

        Mockito.verify(repository).save(eq(user));
        assertEquals(user.getRole(), result.getRole());

        user = new User();
        user.setId(777L);
        user.setName("Another name");
        user.setRole(null);

        Mockito.when(repository.findById(777L)).thenReturn(Optional.empty());
        result = service.addUser(mapper.entityToDto(user));

        assertNull(result);
    }
}