package de.telran.onlineshopforhomeandgarden1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.onlineshopforhomeandgarden1.config.SecurityConfig;
import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.security.JwtProvider;
import de.telran.onlineshopforhomeandgarden1.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@Import(SecurityConfig.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService service;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;


    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    public void getCategoriesAdmin() throws Exception {
        List<CategoryResponseDto> categories = Arrays.asList(new CategoryResponseDto("Category1", "Image1"),
                new CategoryResponseDto("Category2", "Image2"));

        when(service.getAll()).thenReturn(categories);

        mockMvc.perform(get("/categories").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categories.size()));
        Mockito.verify(service).getAll();

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    public void getCategoriesCustomer() throws Exception {
        List<CategoryResponseDto> categories = Arrays.asList(new CategoryResponseDto("Category1", "Image1"),
                new CategoryResponseDto("Category2", "Image2"));

        when(service.getAll()).thenReturn(categories);

        mockMvc.perform(get("/categories").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categories.size()));
        Mockito.verify(service).getAll();

    }


    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    public void addCategory() throws Exception {

        CategoryRequestDto newCategory = new CategoryRequestDto("1L", "Category1", "ImageCategory1");

        when(service.addCategory(any(CategoryRequestDto.class))).thenReturn(newCategory);
        String categoryJs = new ObjectMapper().writeValueAsString(newCategory);
        MvcResult mvcResult = mockMvc.perform(post("/categories").contentType("application/json").content(categoryJs))
                .andDo(print()).andExpect(status().isCreated()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        CategoryRequestDto categoryRequestDto = new ObjectMapper().readValue(contentAsString, CategoryRequestDto.class);
        assertThat(categoryRequestDto).isEqualTo(newCategory);

    }
    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    public void addCategoryBadRequest() throws Exception {

        CategoryRequestDto invalidCategory = new CategoryRequestDto(null, "", "ImageUrl");
        String categoryJson = new ObjectMapper().writeValueAsString(invalidCategory);
         mockMvc.perform(post("/categories").contentType("application/json").content(categoryJson))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

    }


    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    public void updateCategory() throws Exception {

        CategoryRequestDto updatedCategory = new CategoryRequestDto("1L", "Category1", "ImageCategory1");

        when(service.updateCategory(eq(1L), any(CategoryRequestDto.class))).thenReturn(updatedCategory);
        String categoryJs = new ObjectMapper().writeValueAsString(updatedCategory);
        MvcResult mvcResult = mockMvc.perform(put("/categories/{categoryId}", "1").contentType("application/json").content(categoryJs))
                .andDo(print()).andExpect(jsonPath("$.name").value(updatedCategory.getName()))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        CategoryRequestDto categoryRequestDto = new ObjectMapper().readValue(contentAsString, CategoryRequestDto.class);
        assertThat(categoryRequestDto).isEqualTo(updatedCategory);

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    public void updateCategoryNotFound() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto category = new CategoryRequestDto(categoryId.toString(), "Test Category", "Test ImageUrl");
        Mockito.when(service.updateCategory(eq(categoryId), any(CategoryRequestDto.class))).thenReturn(null);
        String categoryJson = new ObjectMapper().writeValueAsString(category);
        mockMvc.perform(put("/categories/{categoryId}", categoryId).contentType("application/json").content(categoryJson))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    public void updateCategoryBadRequest() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto invalidCategory = new CategoryRequestDto(categoryId.toString(), "", null);
        String categoryJson = new ObjectMapper().writeValueAsString(invalidCategory);
        mockMvc.perform(put("/categories/{categoryId}", categoryId).contentType("application/json").content(categoryJson))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    public void deleteCategory() throws Exception {
        Long categoryId = 1L;

        Mockito.doNothing().when(service).delete(categoryId);
        mockMvc.perform(delete("/categories/{categoryId}", categoryId)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        Mockito.verify(service).delete(eq(categoryId));

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMINISTRATOR"})
    public void deleteCategoryNotFound() throws Exception {
        Long categoryId = 1L;

        Mockito.doThrow(new EntityNotFoundException("Category not found")).when(service).delete(eq(categoryId));
        mockMvc.perform(delete("/categories/{categoryId}", categoryId)).andDo(print())
                .andExpect(status().isNotFound()).andReturn();
        Mockito.verify(service).delete(eq(categoryId));

    }
}