package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private Long id;
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @Positive
    private Integer duration;
    private Integer rate;
    private LocalDate releaseDate;
    private Mpa mpa;
    private Set<Long> likes;
    private Set<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("DURATION", duration);
        values.put("RATE", rate);
        values.put("RELEASE_DATE", releaseDate);
        values.put("MPA_ID", mpa.getId());
        return values;
    }
}
