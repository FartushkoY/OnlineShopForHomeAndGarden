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
    void saveUserIsSuccessful() throws Exception {
        UserRequestDto user = new UserRequestDto();
        user.setId(1L);
        user.setEmail("TestEmail@gmail.com");
        user.setName("User Name");
        user.setPhoneNumber("+491715247368");
        user.setRole("CUSTOMER");
        user.setPassword("testPasswordHash");

        Mockito.when(service.saveUser(any(UserRequestDto.class))).thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        Mockito.verify(service).saveUser(any(UserRequestDto.class));
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    void saveUserAsAdministratorIsSuccessful() throws Exception {
        UserRequestDto user = new UserRequestDto();
        user.setId(1L);
        user.setEmail("TestEmail@gmail.com");
        user.setName("User Name");
        user.setPhoneNumber("+491715247368");
        user.setRole("Administrator");
        user.setPassword("testPasswordHash");

        Mockito.when(service.saveUser(any(UserRequestDto.class))).thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        Mockito.verify(service).saveUser(any(UserRequestDto.class));
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER","ADMINISTRATOR"})
    void updateUserIsSuccessful() throws Exception {
        UserRequestDto user = new UserRequestDto();
        user.setId(1L);
        user.setEmail("TestEmail@gmail.com");
        user.setName("User Name");
        user.setPhoneNumber("+491715247368");
        user.setPassword("testPasswordHash");
        user.setRole(String.valueOf(Role.CUSTOMER));

        Mockito.when(service.updateUser(any(), any())).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        Mockito.verify(service).updateUser(Mockito.eq(user.getId()), any(UserRequestDto.class));
    }


    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER","ADMINISTRATOR"})
    void updateUserWithStatusNotFound() throws Exception {
        UserRequestDto user = new UserRequestDto();
        user.setId(1L);
        user.setEmail("TestEmail@gmail.com");
        user.setName("User Name");
        user.setPhoneNumber("+491715247368");
        user.setRole("CUSTOMER");
        user.setPassword("TestUpdating");

        Mockito.when(service.updateUser(15L, user)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/{userId}", 15L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        Mockito.verify(service).updateUser(eq(15L), any(UserRequestDto.class));

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER", "ADMINISTRATOR"})
    void deleteUserIsSuccessful() throws Exception {
        User user = new User();
        user.setId(1L);

        Mockito.when(service.removeUser(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/{userId}", user.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(service).removeUser(user.getId());

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER","ADMINISTRATOR"})
    void deleteUserWithStatusNotFound() throws Exception {
        Mockito.when(service.removeUser(100L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/users/{userId}", 100L)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        Mockito.verify(service).removeUser(100L);
    }
}