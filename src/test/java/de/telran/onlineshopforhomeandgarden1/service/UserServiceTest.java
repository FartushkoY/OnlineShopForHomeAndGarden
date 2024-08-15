package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;


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
    public void savedUser() {
        User user = new User();
        user.setName("Test Name");
        user.setEmail("test@test.eu");
        user.setPhoneNumber("+491715207968");
        user.setRole(Role.valueOf("ADMINISTRATOR"));

        Mockito.when(repository.save(user)).thenReturn(user);
        service.savedUser(mapper.entityToDto(user));
        Mockito.verify(repository).save(eq(user));
        assertEquals(user.getRole(),Role.ADMINISTRATOR);

        user.setRole(Role.valueOf("CUSTOMER"));
        Mockito.when(repository.save(user)).thenReturn(user);
        service.savedUser(mapper.entityToDto(user));
        Mockito.verify(repository).save(eq(user));
        assertEquals(user.getRole(),Role.CUSTOMER);

    }
}