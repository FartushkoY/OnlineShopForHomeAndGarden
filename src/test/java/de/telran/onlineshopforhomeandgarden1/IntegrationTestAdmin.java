package de.telran.onlineshopforhomeandgarden1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.onlineshopforhomeandgarden1.dto.request.*;
import de.telran.onlineshopforhomeandgarden1.dto.response.CategoryResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductWithDiscountPriceResponseDto;
import de.telran.onlineshopforhomeandgarden1.repository.*;
import liquibase.exception.DatabaseException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTestAdmin {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Order(1)
    @Test
    @WithMockUser(username = "Test admin", roles = {"ADMINISTRATOR"})
    public void testAdminOperations() throws Exception {
        CategoryRequestDto newCategory = new CategoryRequestDto(null, "Test category", "Test picture");
        MvcResult categoryResult = mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test category")).andReturn();

        String content = categoryResult.getResponse().getContentAsString();
        CategoryRequestDto createdCategory = objectMapper.readValue(content, CategoryRequestDto.class);
        Long categoryId = Long.valueOf(createdCategory.getId());

        createdCategory.setImageUrl("http://telrun.de/test1");

        createdCategory.setName("Updated category");
        mockMvc.perform(put("/categories/" + categoryId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createdCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated category"));

        ProductRequestDto newProduct = new ProductRequestDto(null, "Test product", "Product description", BigDecimal.valueOf(Double.parseDouble("10.00")), categoryId.toString(), "Test description");
        MvcResult productResult = mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test product")).andReturn();

        String productContent = productResult.getResponse().getContentAsString();
        ProductRequestDto retrievedProduct = objectMapper.readValue(productContent, ProductRequestDto.class);

        ProductRequestDto updatedProduct = retrievedProduct;
        updatedProduct.setName("Updated product");
        updatedProduct.setPrice(BigDecimal.valueOf(Double.parseDouble("10.05")));
        mockMvc.perform(put("/products/" + retrievedProduct.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated product"))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(Double.parseDouble("10.05"))));

        ProductWithDiscountPriceResponseDto productWithDiscount = new ProductWithDiscountPriceResponseDto(
                null,
                "Product with discount", "Product description",
                BigDecimal.valueOf(Double.parseDouble("10.00")),
                BigDecimal.valueOf(Double.parseDouble("5.00")), "Product picture", Instant.now());
        mockMvc.perform(put("/products/addDiscount/" + retrievedProduct.getId() + "?discountPrice=5.00")
                .contentType("application/json")).andExpect(status().isOk());

        mockMvc.perform(get("/products/" + retrievedProduct.getId())
                        .contentType("application/json"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated product"))
                .andExpect(jsonPath("$.discountPrice").value("5"));


        LocalDate startDate = LocalDate.of(2024, 9, 2);
        String period = "MONTH";
        Integer duration = 1;
        String detailing = "DAY";

        mockMvc.perform(get("/orders/revenueReport").param("startDate", startDate.toString())
                .param("period", period).param("duration", duration.toString()).param("detailing", detailing)
                .contentType("application/json")).andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/products/top10Canceled"))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isArray());

        mockMvc.perform(delete("/categories/" + categoryId))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/products/" + retrievedProduct.getId()))
                .andExpect(status().isOk());

    }

    @Order(2)
    @Test

    public void testCustomerOperations() throws Exception {
        WithMockJwtAuthentication withMockJwtAuthentication = new WithMockJwtAuthentication();
        withMockJwtAuthentication.beforeTestMethod(null);

        UserRequestDto newUser = new UserRequestDto(null, "Mr Customer", "test@qmail.com", "+491669413351", "ananas345!", "CUSTOMER");
        MvcResult mvcResult = mockMvc.perform(post("/users/register").contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        UserRequestDto registeredUser = objectMapper.readValue(response, UserRequestDto.class);
        Long userId = registeredUser.getId();

        registeredUser.setPhoneNumber("+491669413322");

        mockMvc.perform(put("/users/" + userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registeredUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("+491669413322"));

        MvcResult categoriesResult = mockMvc.perform(get("/categories").contentType("application/json"))
                .andExpect(status().isOk()).andReturn();

        String categoriesStr = categoriesResult.getResponse().getContentAsString();
        List<CategoryResponseDto> categoryList = objectMapper.readValue(categoriesStr, new TypeReference<List<CategoryResponseDto>>() {
        });
        CategoryResponseDto category = categoryList.get(categoryList.size() - 1);

        mockMvc.perform(get("/products")
                        .param("categoryId", String.valueOf(category.getId()))
                        .param("hasDiscount", "true")
                        .param("minPrice", "9")
                        .param("maxPrice", "12")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());


        MvcResult productOfTheDayResult = mockMvc.perform(get("/products/productOfTheDay")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountPrice").exists()).andReturn();
        ProductWithDiscountPriceResponseDto productOfTheDay = objectMapper.readValue(productOfTheDayResult.getResponse().getContentAsString(),ProductWithDiscountPriceResponseDto.class);
        CartItemRequestDto cartItem = new CartItemRequestDto(productOfTheDay.getId().toString(), 2);
        MvcResult addItemResult = mockMvc.perform(post("/cart")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isCreated()).andReturn();

        String addItemContent = addItemResult.getResponse().getContentAsString();
        CartRequestDto addedItem = objectMapper.readValue(addItemContent, CartRequestDto.class);
        mockMvc.perform(get("/cart")
                        .contentType("application/json"))
                .andExpect(status().isOk());


//        CartItemRequestDto updatedCartItem = new CartItemRequestDto(productRequestDto.getId().toString(), 5);
//        mockMvc.perform(put("/cart")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(updatedCartItem)))
//                .andExpect(status().isOk());
////                .andExpect(jsonPath("$.quantity").value(5));


        OrderItemRequestDto oi1 = new OrderItemRequestDto(2, productOfTheDay.getId().toString());
        List<OrderItemRequestDto> orderItems = new ArrayList<>();
        orderItems.add(oi1);
        OrderRequestDto newOrder = new OrderRequestDto(null, "LessingerStr 4, Berlin 67823", "STANDARD", orderItems);

        MvcResult orderResult = mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated()).andReturn();


        MvcResult orderHistoryResult = mockMvc.perform(get("/orders/history")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andReturn();


        String orderStr = orderHistoryResult.getResponse().getContentAsString();
        List<OrderResponseDto> orders = objectMapper.readValue(orderStr, new TypeReference<List<OrderResponseDto>>() {});
        OrderResponseDto order = orders.get(orders.size() - 1);

        mockMvc.perform(delete("/orders/" + order.getId().toString())
                .contentType("application/json")).andExpect(status().isOk());

        JdbcTestUtils.deleteFromTables(jdbcTemplate,"cart_items", "carts");
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "users", "email like 'test@qmail.com'");
    }

}


