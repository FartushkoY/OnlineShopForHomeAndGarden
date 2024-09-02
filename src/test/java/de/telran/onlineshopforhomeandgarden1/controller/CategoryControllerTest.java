package de.telran.onlineshopforhomeandgarden1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.onlineshopforhomeandgarden1.dto.request.CategoryRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService service;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void getCategories() throws Exception {
        List<CategoryResponseDto> categories = Arrays.asList(new CategoryResponseDto("Category1", "Image1"),
                new CategoryResponseDto("Category2", "Image2"));

        when(service.getAll()).thenReturn(categories);

        mockMvc.perform(get("/categories").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categories.size()));
        Mockito.verify(service).getAll();

    }

    @Test
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
    public void addCategory_BadRequest() throws Exception {

        CategoryRequestDto invalidCategory = new CategoryRequestDto(null, "", "Image1");
        String categoryJson = new ObjectMapper().writeValueAsString(invalidCategory);
         mockMvc.perform(post("/categories").contentType("application/json").content(categoryJson))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
    }


    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategory() {
    }
}