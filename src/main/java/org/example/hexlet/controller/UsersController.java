package org.example.hexlet.controller;

import static io.javalin.rendering.template.TemplateUtil.model;

import io.javalin.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.hexlet.util.NamedRoutes;
import org.example.hexlet.dto.errors.ErrorResponse;
import org.example.hexlet.dto.users.BuildUserPage;
import org.example.hexlet.dto.users.EditUserPage;
import org.example.hexlet.dto.users.UserPage;
import org.example.hexlet.dto.users.UsersPage;
import org.example.hexlet.model.User;
import org.example.hexlet.repository.UserRepository;
import io.javalin.http.Context;


public class UsersController {
    // Обработчик запроса на отображение сводной страницы пользователей
    public static void showAll(Context ctx) {
        var users = UserRepository.getEntities();
        var page = new UsersPage(users);
        ctx.render("users/index.jte", model("page", page));
    }


    // Обработчик запроса на отображение страницы пользователя
    public static void show(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            var user = UserRepository.find(id);

            if (user.isPresent()) {
                var page = new UserPage(user.get());
                ctx.render("users/show.jte", model("page", page));
                return;
            }
        }

        ErrorResponse.sendErrors(ctx,"Internal Server Error", 500, "Некорректный id: " + sid);
    }


    // Обработчик запроса на отображение формы добавления пользователя
    public static void build(Context ctx) {
        var page = new BuildUserPage();
        ctx.render("users/build.jte", model("page", page));
    }


    // Обработчик запроса на создание пользователя
    public static void create(Context ctx) {
        String name = ctx.formParam("name");
        String email = ctx.formParam("email");
        String password = "";
        String passwordConfirmation;

        try {
            name = StringUtils.capitalize(
                    ctx.formParamAsClass("name", String.class)
                            .check(sname -> !sname.isBlank(), "Некорректное имя")
                            .get()
                            .trim()
                            .toLowerCase());

            email = ctx.formParamAsClass("email", String.class)
                    .check( eml-> !eml.isBlank() && eml.contains("@"), "Некорретный адрес электронной почты")
                    .get()
                    .trim()
                    .toLowerCase();

            passwordConfirmation = ctx.formParam("passwordConfirmation");
            password = ctx.formParamAsClass("password", String.class)
                    .check(value -> value.equals(passwordConfirmation), "Пароли не совпадают")
                    .get();

        } catch (ValidationException e) {
            var page = new BuildUserPage(name, email, e.getErrors());
            ctx.render("users/build.jte", model("page", page));
            return;
        }

        var user = new User(name, email, password);

        UserRepository.save(user);

        ctx.redirect(NamedRoutes.usersPath());
    }


    // Обработчик запроса на редактирование пользователя
    public static void editData(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            var userSearchResult = UserRepository.find(id);

            if (userSearchResult.isPresent()) {
                var user = userSearchResult.get();
                var page = new EditUserPage(id, user.getName(), user.getEmail(), null);

                ctx.render("users/edit.jte", model("page", page));

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
            String email = ctx.formParam("email");
            String password = "";
            String passwordConfirmation;

            try {
                name = StringUtils.capitalize(
                        ctx.formParamAsClass("name", String.class)
                                .check(sname -> !sname.isBlank(), "Некорректное имя")
                                .get()
                                .trim()
                                .toLowerCase());

                email = ctx.formParamAsClass("email", String.class)
                        .check( eml-> !eml.isBlank() && eml.contains("@"), "Некорретный адрес электронной почты")
                        .get()
                        .trim()
                        .toLowerCase();

                passwordConfirmation = ctx.formParam("passwordConfirmation");
                password = ctx.formParamAsClass("password", String.class)
                        .check(value -> value.equals(passwordConfirmation), "Пароли не совпадают")
                        .get();

                var userSearchResult = UserRepository.find(id);

                if (userSearchResult.isPresent()) {
                    var user = userSearchResult.get();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);

                    ctx.redirect(NamedRoutes.usersPath());

                    return;
                }

            } catch (ValidationException e) {
                var page = new EditUserPage(id, name, email, e.getErrors());
                ctx.render("users/edit.jte", model("page", page));
                return;
            }
        }

        ErrorResponse.sendErrors(ctx,"Internal Server Error", 500, "Некорректный id: " + sid);
    }


    // Обработчик запроса на удаление пользователя
    public static void delete(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            var result = UserRepository.delete(id);

            if (result) {
                ctx.redirect(NamedRoutes.usersPath());
                return;
            }
        }

        ErrorResponse.sendErrors(ctx,"Internal Server Error", 500, "Некорректный id: " + sid);
    }
}
