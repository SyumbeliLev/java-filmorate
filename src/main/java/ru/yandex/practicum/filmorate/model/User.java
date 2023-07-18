package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    public int id;
    private String email;
    private String login;

    private LocalDate birthday;

    @Builder.Default
    private String name = null;
}
