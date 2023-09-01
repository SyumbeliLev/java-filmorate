package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class User {
    public Long id;
    @Builder.Default
    private String name = null;
    @NotBlank
    private String login;
    @Past
    private LocalDate birthday;
    @Email
    @NotBlank
    private String email;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", name);
        values.put("LOGIN", login);
        values.put("BIRTHDAY", birthday);
        values.put("RELEASE_DATE", email);
        values.put("EMAIL", email);
        return values;
    }
}
