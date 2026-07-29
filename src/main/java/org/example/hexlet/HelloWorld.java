package org.example.hexlet;

import io.javalin.Javalin;

public class HelloWorld {
    public static void main(String[] args) {
        // Создаем приложение
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });
        // Описываем, что загрузится по адресу /
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
        app.start(7070); // Стартуем веб-сервер
    }
}
