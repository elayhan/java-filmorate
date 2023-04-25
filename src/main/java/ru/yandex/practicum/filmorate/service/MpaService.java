package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa findMpaById(int id) {
        log.debug("Получаем Возрастной рейтинг с id {}", id);
        return mpaStorage.findMpaById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Возрастной рейтинг с id %s не найден", id)));
    }

    public List<Mpa> getAllMpa() {
        log.debug("Получаем список всех рейтингов");
        return mpaStorage.getAllMpa();
    }
}
