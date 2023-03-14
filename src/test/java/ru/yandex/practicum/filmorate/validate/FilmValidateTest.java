package ru.yandex.practicum.filmorate.validate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmValidateTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Test
    void releaseDateBeforeMinDateTest() {
        Film film = Film.builder()
                .name("1")
                .duration(2)
                .releaseDate(LocalDate.of(1800, 1, 1))
                .build();

        FilmException ex = assertThrows(FilmException.class, () -> new FilmController().createFilm(film));
        assertEquals("Указана некорректная дата релиза!", ex.getMessage());
    }

    @Test
    void createNoNameFilmTest() throws Exception {
        Film film = Film.builder()
                .releaseDate(LocalDate.of(2000,1,1))
                .duration(1)
                .build();

        mvc.perform(
                post("/films")
                        .content(mapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(s -> s.getResolvedException().getClass().equals(DefaultHandlerExceptionResolver.class));
    }

    @Test
    void createOverSizeDescriptionFilmTest() throws Exception {
        Film film = Film.builder()
                .name("film")
                .releaseDate(LocalDate.of(2000,1,1))
                .duration(1)
                .description(String.format("%201s", ""))
                .build();

        mvc.perform(
                post("/films")
                        .content(mapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(s -> s.getResolvedException().getClass().equals(DefaultHandlerExceptionResolver.class));
    }
    @Test
    void createNegativeDurationFilmTest() throws Exception {
        Film film = Film.builder()
                .name("film")
                .releaseDate(LocalDate.of(2000,1,1))
                .duration(-1)
                .description(String.format("%2s", ""))
                .build();

        mvc.perform(
                post("/films")
                        .content(mapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(s -> s.getResolvedException().getClass().equals(DefaultHandlerExceptionResolver.class));
    }





}
