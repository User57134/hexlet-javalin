package org.example.hexlet;

import org.jetbrains.annotations.NotNull;

public class NamedRoutes {

    public static String usersPath() {
        return "/users";
    }

    public static String userPath(Long id) {
        return userPath(String.valueOf(id));
    }

    public static String userPath(String id) {
        return "/users/" + id;
    }

    public static String userEditPath(Long id) {
        return userEditPath(String.valueOf(id));
    }

    public static String userEditPath(String id) {
        return "/users/" + id + "/edit";
    }

    public static String buildUserPath() {
        return "/users/build";
    }

    public static String coursesPath() {
        return "/courses";
    }

    // Это нужно, чтобы не преобразовывать типы снаружи
    public static String coursePath(Long id) {
        return coursePath(String.valueOf(id));
    }

    public static String coursePath(String id) {
        return "/courses/" + id;
    }

    public static String buildCoursePath() {
        return "/courses/build";
    }
}
