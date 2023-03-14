package ru.yandex.practicum.filmorate.validate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserValidateTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void createNotValidEmailTest() throws Exception{
        User user = User.builder()
                .email("@@@1111@@@@")
                .login("sd")
                .name("ds")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        mvc.perform(
                post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(s -> s.getResolvedException().getClass().equals(DefaultHandlerExceptionResolver.class));
    }
    @Test
    void createIsBlankEmailTest() throws Exception{
        User user = User.builder()
                .email("")
                .login("sd")
                .name("ds")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        mvc.perform(
                post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(s -> s.getResolvedException().getClass().equals(DefaultHandlerExceptionResolver.class));
    }
    @Test
    void createSpaceLoginTest() throws Exception{
        User user = User.builder()
                .email("123@321.org")
                .login(" sd")
                .name("ds")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        mvc.perform(
                post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(s -> s.getResolvedException().getClass().equals(DefaultHandlerExceptionResolver.class));
    }
    @Test
    void createNoNameTest() throws Exception{
        User user = User.builder()
                .email("123@321.org")
                .login("sd")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        mvc.perform(
                post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("sd"));
    }

    @Test
    void createFeatureBirthdayTest() throws Exception{
        User user = User.builder()
                .email("123@321.org")
                .login("sd")
                .name("ds")
                .birthday(LocalDate.of(3000,1,1))
                .build();

        mvc.perform(
                        post("/users")
                                .content(mapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(s -> s.getResolvedException().getClass().equals(DefaultHandlerExceptionResolver.class));
    }



}
