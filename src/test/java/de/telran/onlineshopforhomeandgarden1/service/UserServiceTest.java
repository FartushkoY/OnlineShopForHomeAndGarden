package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
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
import static org.mockito.ArgumentMatchers.any;
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
        service.saveUser(mapper.entityToRequestDto(user));
        Mockito.verify(repository).save(eq(user));
        assertEquals(user.getRole(), Role.ADMINISTRATOR);


        user.setRole(Role.valueOf("CUSTOMER"));
        Mockito.when(repository.save(user)).thenReturn(user);
        service.saveUser(mapper.entityToRequestDto(user));
        Mockito.verify(repository).save(eq(user));
        assertEquals(user.getRole(), Role.CUSTOMER);


        user.setRole(null);
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);
        service.saveUser(mapper.entityToRequestDto(user));
        user.setRole(Role.CUSTOMER);
        Mockito.verify(repository, Mockito.times(2)).save(eq(user));
        assertEquals(user.getRole(), Role.CUSTOMER);
    }

    @Test
    public void updateUser() {
        User userOld = new User();
        userOld.setId(2L);
        userOld.setName("Old name");
        userOld.setEmail("test@test.eu");
        userOld.setPhoneNumber("+491715207968");
        userOld.setRole(Role.valueOf("ADMINISTRATOR"));
        userOld.setPasswordHash("TestPassHash123");

        User updatedUser = new User();
        updatedUser.setId(2L);
        updatedUser.setName("New name");
        updatedUser.setEmail("test@test.eu");
        updatedUser.setPhoneNumber("+491710001968");
        updatedUser.setRole(Role.valueOf("CUSTOMER"));
        updatedUser.setPasswordHash("TestPassHash123");

        Mockito.when(repository.findById(updatedUser.getId())).thenReturn(Optional.of(userOld));
        Mockito.when(repository.save(updatedUser)).thenReturn(updatedUser);
        service.updateUser(2L, mapper.entityToRequestDto(updatedUser));
        Mockito.verify(repository).save(eq(updatedUser));

// Not Found

        User user = new User();
        user.setId(547L);

        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());
        Optional<UserRequestDto> result = service.updateUser(547L, mapper.entityToRequestDto(user));
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeUser() {
        User user = new User();
        user.setId(1L);

        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        service.removeUser(user.getId());
        Mockito.verify(repository).deleteById(user.getId());

        // Not Found

        Mockito.when(repository.findById(1000L)).thenReturn(Optional.empty());
        Mockito.verify(repository, Mockito.never()).deleteById(1000L);
    }
}