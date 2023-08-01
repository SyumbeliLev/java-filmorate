package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    public int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String login;
    @Past
    private LocalDate birthday;
    @Builder.Default
    private String name = null;
    @Builder.Default
    private Set<Long> friends = new HashSet<>();



}
