package org.example.hexlet.controller;

import static io.javalin.rendering.template.TemplateUtil.model;

import io.javalin.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.hexlet.NamedRoutes;
import org.example.hexlet.dto.errors.ErrorResponse;
import org.example.hexlet.dto.users.BuildUserPage;
import org.example.hexlet.dto.users.UserPage;
import org.example.hexlet.dto.users.UsersPage;
import org.example.hexlet.model.User;
import org.example.hexlet.repository.UserRepository;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class UsersController {
    public static void showAllUsers(Context ctx) {
        var users = UserRepository.getEntities();
        var page = new UsersPage(users);
        ctx.render("users/index.jte", model("page", page));
    }


    public static void showUser(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var user = UserRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var page = new UserPage(user);
        ctx.render("users/show.jte", model("page", page));
    }


    public static void buildUser(Context ctx) {
        var page = new BuildUserPage();
        ctx.render("users/build.jte", model("page", page));
    }

    // Обработчик запроса на создание пользователя
    public static void createUser(Context ctx) {
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
    public static void editUserData(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            var user = UserRepository.find(id);

            if (user.isPresent()) {
                var page = new BuildUserPage(user.get().getName(), user.get().getEmail(), null);
                ctx.render("users/edit.jte", model("page", page));
                return;
            } else {
                throw new NotFoundResponse("Entity with id = " + id + " not found");
            }
        }
    }

    // Обработчик запроса на обновление данных пользователя
    public static void updateUser(Context ctx) {
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

            } catch (ValidationException e) {
                var page = new BuildUserPage(name, email, e.getErrors());
                ctx.render("users/build.jte", model("page", page));
                return;
            }

            var userSearchResult = UserRepository.find(id);

            if (userSearchResult.isPresent()) {
                var user = userSearchResult.get();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);

                UserRepository.save(user);

                ctx.redirect(NamedRoutes.usersPath());
            } else {
                throw new NotFoundResponse("Entity with id = " + id + " not found");
            }
        }
    }

    // Обработчик запроса на удаление пользователя
    public static void deleteUser(Context ctx) {
        var sid = ctx.pathParam("id");

        long id = NumberUtils.toLong(sid, 0L);
        if (id != 0) {
            var result = UserRepository.delete(id);

            if (result) {
                ctx.redirect(NamedRoutes.usersPath());
                return;
            } else {
                var resp = new ErrorResponse(
                        "Internal Server Error"
                        , 500
                        , "Пользователя с id = " + id + " не существует");

                ctx.status(500);
                ctx.json(resp);
            }
        }
    }
}
