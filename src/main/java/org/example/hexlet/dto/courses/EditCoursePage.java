package org.example.hexlet.dto.courses;

import io.javalin.validation.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EditCoursePage {
    private Long id;
    private String name;
    private String description;
    private Map<String, List<ValidationError<Object>>> errors;

    public EditCoursePage(String name, String description,  Map<String, List<ValidationError<Object>>> errors) {
        this.name = name;
        this.description = description;
        this.errors = errors;
    }
}
