package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.CartDto;
import de.telran.onlineshopforhomeandgarden1.dto.UserDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.mapper.CartMapper;
import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
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
    private static UserRepository userRepository;
    private static CartRepository cartRepository;
    private static UserMapper userMapper;
    private static CartMapper cartMapper;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        cartRepository = Mockito.mock(CartRepository.class);
        userMapper = Mappers.getMapper(UserMapper.class);
        cartMapper = Mappers.getMapper(CartMapper.class);
        service = new UserService(userRepository, cartRepository, userMapper);
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
        Mockito.when(userRepository.save(Mockito.eq(user))).thenReturn(user);
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

        assertEquals("Old name", user.getName());
        assertEquals("+491724706923", user.getPhoneNumber());

        Mockito.when(userRepository.findById(574L)).thenReturn(Optional.empty());
        Optional<UserDto> optional = service.updateUser(547L, "name", "+491530249951");
        assertTrue(optional.isEmpty());

    }

    @Test
    public void removeUser() {
        Long userId = 1L;
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        Mockito.when(cartRepository.findById(cartDto.getId())).thenReturn(Optional.of(cartMapper.dtoToEntity(cartDto)));

        service.removeUser(cartDto, userId);
        Mockito.verify(cartRepository).deleteByUserId(userId);
        Mockito.verify(userRepository).deleteById(userId);

        Mockito.when(cartRepository.findById(500L)).thenReturn(Optional.empty());
        service.removeUser(null,userId);


    }
}