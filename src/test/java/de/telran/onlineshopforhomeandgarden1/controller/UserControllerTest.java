package de.telran.onlineshopforhomeandgarden1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.onlineshopforhomeandgarden1.config.SecurityConfig;
import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Role;
import de.telran.onlineshopforhomeandgarden1.security.JwtProvider;
import de.telran.onlineshopforhomeandgarden1.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @MockBean
    private UserService service;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER,ADMIN"})
    void saveUserIsSuccessful() throws Exception {
        UserRequestDto user = new UserRequestDto();
        user.setId(1L);
        user.setEmail("TestEmail@gmail.com");
        user.setName("User Name");
        user.setPhoneNumber("+491715247368");
        user.setRole("CUSTOMER");
        user.setPasswordHash("testPasswordHash");

        when(service.saveUser(any(UserRequestDto.class))).thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        verify(service).saveUser(any(UserRequestDto.class));
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER,ADMIN"})
    void saveUserIsUnsuccessful() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setId(1L);
        userRequestDto.setEmail("TestEmail@gmail.com");
        userRequestDto.setName("User45 Name");
        userRequestDto.setPasswordHash("testPasswordHash");
        userRequestDto.setPhoneNumber("+151729764458");

        Mockito.when(service.saveUser(any(UserRequestDto.class)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());

        verify(service, Mockito.never()).saveUser(any(UserRequestDto.class));
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER,ADMIN"})
    void updateUserIsSuccessful() throws Exception {
        UserRequestDto user = new UserRequestDto();
        user.setId(1L);
        user.setEmail("TestEmail@gmail.com");
        user.setName("User Name");
        user.setPhoneNumber("+491715247368");
        user.setPasswordHash("testPasswordHash");
        user.setRole(String.valueOf(Role.CUSTOMER));


        when(service.updateUser(any(), any())).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(service).updateUser(Mockito.eq(user.getId()), any(UserRequestDto.class));
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER,ADMIN"})
    void updateUserIsUnsuccessful() throws Exception {
        UserRequestDto user = new UserRequestDto();
        user.setId(1L);
        user.setEmail("TestEmail@gmail.com");
        user.setName("User45 Name");
        user.setPhoneNumber("+151715247368");
        user.setRole("CUSTOMER");

        Mockito.when(service.updateUser(Mockito.eq(1L), any(UserRequestDto.class)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        verify(service, Mockito.never()).updateUser(Mockito.eq(1L), any(UserRequestDto.class));

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER,ADMIN"})
    void updateUserIsImpossible() throws Exception {
        UserRequestDto user = new UserRequestDto();
        user.setId(1L);
        user.setEmail("TestEmail@gmail.com");
        user.setName("User Name");
        user.setPhoneNumber("+491715247368");
        user.setRole("CUSTOMER");

        when(service.updateUser(15L, user)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/{userId}", 15L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(service).updateUser(eq(15L), any(UserRequestDto.class));

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER,ADMIN"})
    void deleteUserIsSuccessful() throws Exception {
        User user = new User();
        user.setId(1L);

        when(service.removeUser(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).removeUser(user.getId());

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER,ADMIN"})
    void deleteUserIsUnsuccessful() throws Exception {
        User user = new User();
        user.setId(1L);

        when(service.removeUser(100L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/users/{userId}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).removeUser(100L);
    }
}