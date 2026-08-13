package org.example.hexlet.dto.users;

import java.util.List;
import java.util.Map;

import io.javalin.validation.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BuildUserPage {
    private Long id;
    private String name;
    private String email;
    private Map<String, List<ValidationError<Object>>> errors;

    public BuildUserPage(String name, String email, Map<String, List<ValidationError<Object>>> errors) {
        id = null;
        this.name = name;
        this.email = email;
        this.errors = errors;
    }

    public BuildUserPage(String name, String email) {
        this(name, email, null);
    }
}