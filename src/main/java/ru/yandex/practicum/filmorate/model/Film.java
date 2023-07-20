package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
