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
    public void savedUser() {
        User user = new User();
        user.setName("Test Name");
        user.setEmail("test@test.eu");
        user.setPhoneNumber("+491715207968");
        user.setRole(Role.valueOf("ADMINISTRATOR"));
        user.setPasswordHash("TestPassHash123");

        Mockito.when(repository.save(user)).thenReturn(user);
        service.saveUser(mapper.entityToDto(user));
        Mockito.verify(repository).save(eq(user));
        assertEquals(user.getRole(), Role.ADMINISTRATOR);

        user.setRole(Role.valueOf("CUSTOMER"));
        Mockito.when(repository.save(user)).thenReturn(user);
        service.saveUser(mapper.entityToDto(user));
        Mockito.verify(repository).save(eq(user));
        assertEquals(user.getRole(), Role.CUSTOMER);

    }

    @Test
    public void updateUser() {
        User user = new User();
        user.setId(2L);
        user.setName("Old name");
        user.setPhoneNumber("+491724706923");

        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(user));
        Mockito.when(repository.save(user)).thenReturn(user);

        assertEquals("Old name", user.getName());
        assertEquals("+491724706923", user.getPhoneNumber());

        Mockito.when(repository.findById(574L)).thenReturn(Optional.empty());
        Optional<UserDto> optional = service.updateUser(547L, "name", "+491530249951");
        assertTrue(optional.isEmpty());

        user.setRole(null);

        Mockito.when(repository.save(Mockito.eq(user))).thenReturn(user);
        service.saveUser(mapper.entityToDto(user));
        user.setRole(Role.CUSTOMER);
        Mockito.verify(repository, Mockito.times(2)).save(eq(user));
        assertEquals(user.getRole(), Role.CUSTOMER);

    }
}