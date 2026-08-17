package org.example.hexlet;


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


    public static String coursePath(Long id) {
        return coursePath(String.valueOf(id));
    }


    public static String coursePath(String id) {
        return "/courses/" + id;
    }


    public static String courseEditPath(Long id) {
        return courseEditPath(String.valueOf(id));
    }


    public static String courseEditPath(String id) {
        return "/courses/" + id + "/edit";
    }


    public static String buildCoursePath() {
        return "/courses/build";
    }


    public static String sessionsPath() {
        return "/sessions";
    }


    public static String sessionsBuildPath() {
        return "/sessions/build";
    }
}
