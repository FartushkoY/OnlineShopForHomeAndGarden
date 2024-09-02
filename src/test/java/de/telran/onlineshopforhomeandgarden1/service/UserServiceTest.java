package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.UserDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private static UserRepository userRepository;
    private static UserMapper userMapper;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        userMapper = Mappers.getMapper(UserMapper.class);
        service = new UserService(userRepository, userMapper);
    }

    @Test
    public void savedUser() {
        User user = new User();
        user.setName("Test Name");
        user.setEmail("test@test.eu");
        user.setPhoneNumber("+491715207968");
        user.setRole(Role.valueOf("ADMINISTRATOR"));
        user.setPasswordHash("TestPassHash123");

        Mockito.when(userRepository.save(user)).thenReturn(user);
        service.saveUser(userMapper.entityToDto(user));
        Mockito.verify(userRepository).save(eq(user));
        assertEquals(user.getRole(), Role.ADMINISTRATOR);

        user.setRole(Role.valueOf("CUSTOMER"));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        service.saveUser(userMapper.entityToDto(user));
        Mockito.verify(userRepository).save(eq(user));
        assertEquals(user.getRole(), Role.CUSTOMER);

        user.setRole(null);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        service.saveUser(userMapper.entityToDto(user));
        user.setRole(Role.CUSTOMER);
        Mockito.verify(userRepository, Mockito.times(2)).save(eq(user));
        assertEquals(user.getRole(), Role.CUSTOMER);
    }

    @Test
    public void updateUser() {
        User user = new User();
        user.setId(2L);
        user.setName("Old name");
        user.setPhoneNumber("+491724706923");

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Optional<UserDto> resultOptional = service.updateUser(user.getId(), user.getName(), user.getPhoneNumber());

        assertEquals(resultOptional.get().getName(), user.getName());
        assertEquals(resultOptional.get().getPhoneNumber(), user.getPhoneNumber());

        Mockito.when(userRepository.findById(574L)).thenReturn(Optional.empty());
        Optional<UserDto> optional = service.updateUser(547L, "name", "+491530249951");
        assertTrue(optional.isEmpty());

    }

    @Test
    public void removeUser() {
        User user = new User();
        user.setId(1L);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        service.removeUser(user.getId());
        Mockito.verify(userRepository).deleteById(user.getId());

        Mockito.when(userRepository.findById(1000L)).thenReturn(Optional.empty());
        Mockito.verify(userRepository, Mockito.never()).deleteById(1000L);
    }
}