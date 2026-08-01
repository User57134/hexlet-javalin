package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.model.Course;
import org.example.hexlet.dto.courses.CoursePage;


import java.util.ArrayList;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class HelloWorld {
    public static void main(String[] args) {

        // Создаем приложение
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        // Описываем, что загрузится по адресу /
        app.get("/", ctx -> {
            ctx.render("index.jte");
        });

        app.get("/users/{id}/post/{postId}", ctx -> {
            var id = ctx.pathParamAsClass("id", String.class).get();
            var postId = ctx.pathParam("postId");

            ctx.result("user id = " + id + ", postId = " + postId);
        });

        app.post("/users", ctx -> ctx.result("POST /users"));
        app.get("/hello", ctx -> {
            var name = ctx.queryParamAsClass("name", String.class).getOrDefault("World");
            ctx.result("Hello, " + name + "!");
        });

        app.get("/courses/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Long.class).getOrDefault(0L);

            Course course = null;
            int intid = id.intValue();

            switch (intid) {
                case 1:
                    course = new Course("Java", "Some description...");
                    break;

                case 2:
                    course = new Course("Python", "Some description...");
                    break;

                case 3:
                    course = new Course("PHP", "Some description...");
                    break;

                default:
                    throw new RuntimeException("not supported course");
            }


            var page = new CoursePage(course);
            ctx.render("courses/selectedCourse.jte", model("page", page));
        });

        app.get("/courses", ctx -> {
            var javaCourse = new Course("Java", "Some description...");
            javaCourse.setId(1L);

            var pythonCourse = new Course("Python", "Some description...");
            pythonCourse.setId(2L);

            var phpCourse = new Course("PHP", "Some description...");
            phpCourse.setId(3L);

            List<Course> courses = new ArrayList<>();
            courses.add(javaCourse);
            courses.add(phpCourse);
            courses.add(pythonCourse);

            System.out.println("Courses IDS:");
            for (var c : courses)
               System.out.println(c.getId());

            var page = new CoursesPage(courses, "Доступные курсы");
            ctx.render("courses/index.jte", model("page", page));
        });

        app.start(7070); // Стартуем веб-сервер
    }
}
