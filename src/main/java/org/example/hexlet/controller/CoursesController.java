package org.example.hexlet.controller;

import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.hexlet.NamedRoutes;
import org.example.hexlet.dto.courses.BuildCoursePage;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.dto.courses.EditCoursePage;
import org.example.hexlet.dto.errors.ErrorResponse;
import org.example.hexlet.model.Course;
import org.example.hexlet.repository.CourseRepository;
import java.util.List;
import static io.javalin.rendering.template.TemplateUtil.model;


public class CoursesController {
    // Обработчик запроса на отображение сводной страницы курсов
    public static void showAll(Context ctx) {
        var term = ctx.queryParam("term");

        var courses = CourseRepository.getEntities();
        List<Course> resultList;

        if (term != null) {
            var searchString = term;

            resultList = courses.stream()
                    .filter(
                            course -> (
                                    course.getName().equals(searchString))
                    ).toList();

            if (resultList.isEmpty()) {
                resultList = courses.stream()
                        .filter(
                                course -> (
                                        course.getDescription().contains(searchString))
                        ).toList();
            }

        } else {
            resultList = courses;
            term = "";
        }

        var page = new CoursesPage(resultList, "Доступные курсы", term);
        ctx.render("courses/index.jte", model("page", page));
    }


    // Обработчик запроса на отображение страницы курса
    public static void show(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            var course = CourseRepository.find(id);

            if (course.isPresent()) {
                var page = new CoursePage(course.get());
                ctx.render("courses/show.jte", model("page", page));
                return;
            }
        }

        ErrorResponse.sendErrors(ctx,"Internal Server Error", 500, "Некорректный id: " + sid);
    }


    // Обработчик запроса на отображение формы добавления курса
    public static void build(Context ctx) {
        var page = new BuildCoursePage();
        ctx.render("courses/build.jte", model("page", page));
    }


    // Обработчик запроса на создание курса
    public static void create(Context ctx) {
        String name = ctx.formParam("name");
        String description = ctx.formParam("description");

        try {
            name = StringUtils.capitalize(
                    ctx.formParamAsClass("name", String.class)
                            .check(sname -> !sname.isBlank(), "Некорректное название курса")
                            .get()
                            .trim());

            description = ctx.formParamAsClass("description", String.class)
                    .get()
                    .trim();

            var course = new Course(name, description);

            CourseRepository.save(course);
            ctx.redirect(NamedRoutes.coursesPath());

        } catch (ValidationException e) {
            var page = new BuildCoursePage(name, description, e.getErrors());
            ctx.render("courses/build.jte", model("page", page));
        }
    }


    // Обработчик запроса на редактирование пользователя
    public static void editData(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            var courseSearchResult = CourseRepository.find(id);

            if (courseSearchResult.isPresent()) {
                var course = courseSearchResult.get();
                var page = new EditCoursePage(id, course.getName(), course.getDescription(), null);

                ctx.render("courses/edit.jte", model("page", page));

                return;
            }
        }

        ErrorResponse.sendErrors(ctx,"Internal Server Error", 500, "Некорректный id: " + sid);
    }


    // Обработчик запроса на обновление данных пользователя
    public static void update(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            String name = ctx.formParam("name");
            String description = ctx.formParam("description");

            try {
                name = StringUtils.capitalize(
                        ctx.formParamAsClass("name", String.class)
                                .check(sname -> !sname.isBlank(), "Некорректное название курса")
                                .get()
                                .trim());

                description = ctx.formParamAsClass("description", String.class)
                        .get()
                        .trim();

                var courseSearchResult = CourseRepository.find(id);

                if (courseSearchResult.isPresent()) {
                    var course = courseSearchResult.get();
                    course.setName(name);
                    course.setDescription(description);

                    ctx.redirect(NamedRoutes.coursesPath());

                    return;
                }

            } catch (ValidationException e) {
                var page = new EditCoursePage(id, name, description, e.getErrors());
                ctx.render("courses/edit.jte", model("page", page));
            }
        }

        ErrorResponse.sendErrors(ctx,"Internal Server Error", 500, "Некорректный id: " + sid);
    }


    // Обработчик запроса на удаление пользователя
    public static void delete(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            var result = CourseRepository.delete(id);

            if (result) {
                ctx.redirect(NamedRoutes.coursesPath());
                return;
            }
        }

        ErrorResponse.sendErrors(ctx,"Internal Server Error", 500, "Некорректный id: " + sid);
    }
}
