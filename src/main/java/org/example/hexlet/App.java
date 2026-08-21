package org.example.hexlet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.apache.commons.text.StringEscapeUtils;
import org.example.hexlet.controller.CoursesController;
import org.example.hexlet.controller.SessionsController;
import org.example.hexlet.controller.UsersController;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.model.Course;
import org.example.hexlet.repository.BaseRepository;
import org.example.hexlet.repository.CourseRepository;
import org.example.hexlet.util.NamedRoutes;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static io.javalin.rendering.template.TemplateUtil.model;


public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static String getDatabaseUrl() {
        // Получаем url базы данных из переменной окружения DATABASE_URL
        // Если она не установлена, используем базу в памяти
        return System.getenv().getOrDefault("DATABASE_URL", "jdbc:h2:mem:hexlet_test");
    }

    public static Javalin getApp() {
        var hikariConfig = new HikariConfig();

        // DB_CLOSE_DELAY = -1 - указание базе H2 закрываться при закрытии приложения,
        // по-умолчанию закрытие базы происходит при закрытии последнего активного соединения
        //hikariConfig.setJdbcUrl("jdbc:h2:mem:hexlet_test;DB_CLOSE_DELAY=-1");
        hikariConfig.setJdbcUrl(getDatabaseUrl());

        var dataSource = new HikariDataSource(hikariConfig);
        BaseRepository.dataSource = dataSource;

        try (var is = App.class.getClassLoader().getResourceAsStream("schema.sql")) {
            if (is != null) {
                var bufferedReader = new BufferedReader(new InputStreamReader(is));
                var sql = bufferedReader.lines().collect(Collectors.joining("\n"));

                try (var connection = dataSource.getConnection()) {
                    var statement = connection.createStatement();

                    statement.execute(sql);
                } catch (SQLException e) {
                    throw new RuntimeException("Database interaction error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Создаем приложение
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        // Этот промежуточный обработчик добавляет дату и время запроса
        app.before(ctx -> {
            var currentTimestamp = LocalDateTime.now();
            log.info("input request received: "  + currentTimestamp);
        });

        // Описываем, что загрузится по адресу /
        app.get("/", ctx -> {
            String currentUser = ctx.sessionAttribute("currentUser");
            var page = new MainPage(currentUser);
            ctx.render("index.jte", model("page", page));
        });

        // Отображение формы создания новой сессии(логин)
        app.get(NamedRoutes.sessionsBuildPath(), SessionsController::build);

        // Процесс логина
        app.post(NamedRoutes.sessionsPath(), ctx -> {
            String method = ctx.formParam("_method");

            if (method != null) {
                if ("DELETE".equalsIgnoreCase(method)) {
                    SessionsController.destroy(ctx);
                    return;
                }
            }

            SessionsController.create(ctx);
        });

        // Процесс выхода из аккаунта
        app.delete(NamedRoutes.sessionsPath(), SessionsController::destroy);

        // Описываем, что загрузится по адресу /hello
        app.get("/hello", ctx -> {
            var name = ctx.queryParamAsClass("name", String.class).getOrDefault("World");
            ctx.result("Hello, " + name + "!");
        });

        // добавление небезопасной (доверяющей данным пользователя) обработки данных:
        // в данном случае предполагается, что пользователь введет корректный 'id';
        // однако пользователь вместо этого для значение 'id' вводит скрипт '<scritp>alert('attack!')</script',
        // закодированный в url в виде '%3Cscript%3Ealert('attack!')%3B%3C%2Fscript%3E'
        app.get("/unsafe/{id}", ctx -> {
            var id = ctx.pathParam("id");
            ctx.contentType("html");
            ctx.result("<h1>" + id + "</h1>");
        });

        // добавление безопасной (проверяющей пользовательские данные) обработки данных:
        // в данном случае предполагается, что все что введет пользователь будет проверено на корректность
        // при помощи библиотеки OWASP Java HTML Sanitizer;
        // пользователь вместо этого для значение 'id' вводит скрипт '<scritp>alert('attack!')</script',
        // закодированный в url в виде '%3Cscript%3Ealert('attack!')%3B%3C%2Fscript%3E'
        app.get("/safe1/{id}", ctx -> {
            var untrustedId = ctx.pathParam("id");

            // Создание набора разрешительных правил для html
            PolicyFactory policy = new HtmlPolicyBuilder()
                    .allowElements("a")
                    .allowUrlProtocols("https")
                    .allowAttributes("href").onElements("a")
                    .requireRelNofollowOnLinks()
                    .toFactory();

            // Приведение к безопасному виду согласно вышеуказанному набору правли
            String safeHTML = policy.sanitize(untrustedId);
            System.out.println("safe html = " + safeHTML);

            // Вывод безопасного html
            ctx.contentType("html");
            ctx.result("<h1>" + safeHTML + "</h1>");
        });


        // добавление безопасной (проверяющей пользовательские данные) обработки данных:
        // в данном случае предполагается, что все что введет пользователь будет проверено на корректность
        // при помощи замены скобок тегов на эквиваленты: < на &lt, > на &gt - тогда браузер воспринимает
        // эту последовательность символов не как начало и конец тега, а как текст;
        // пользователь вместо этого для значение 'id' вводит скрипт '<scritp>alert('attack!')</script',
        // закодированный в url в виде '%3Cscript%3Ealert('attack!')%3B%3C%2Fscript%3E'
        app.get("/safe2/{id}", ctx -> {
            var untrustedId = ctx.pathParam("id");

            // Приведение к безопасному виду
            var escapedId = StringEscapeUtils.escapeHtml4(untrustedId);

            // Вывод безопасного html
            ctx.contentType("html");
            ctx.result("<h1>" + escapedId + "</h1>");
        });

        // Обработка запроса на просмотр страницы со списком всех пользователей
        app.get(NamedRoutes.usersPath(), UsersController::showAll);

        // Обработка запросов на добавление нового пользователя
        app.post(NamedRoutes.usersPath(), UsersController::create);

        // Отображение формы для добавления пользователей
        app.get(NamedRoutes.buildUserPath(), UsersController::build);

        // Обработка запросов на отображение страницы конкретного пользователя из списка
        app.get(NamedRoutes.userPath("{id}"), UsersController::show);

        // Отображение формы редактирования данных пользователя
        app.get(NamedRoutes.userEditPath("{id}"), UsersController::editData);

        // Запросы на обновление и удаление пользователя имеют одинаковый маршрут,
        // так как через тег 'form' из HTML отправляются только GET или POST запросы,
        // поэтому дополнительно используется скрытый элемент с именем '_method'
        // и значением - реальное запрашиваемое действие:
        // <input type="hidden" name="_method" value="PATCH">
        // <input type="hidden" name="_method" value="DELETE">
        app.post(NamedRoutes.userPath("{id}"), ctx -> {
            String method = ctx.formParam("_method");

            if ("PATCH".equalsIgnoreCase(method)) {
                UsersController.update(ctx);

                // Обработка запросов на удаление пользователя
            } else if ("DELETE".equalsIgnoreCase(method)) {
                UsersController.delete(ctx);
            }
        });


        app.get("/users/{id}/post/{postId}", ctx -> {
            var id = ctx.pathParamAsClass("id", String.class).get();
            var postId = ctx.pathParam("postId");

            ctx.result("user id = " + id + ", postId = " + postId);
        });

        // Обработка запроса на просмотр страницы со списком всех курсов
        app.get(NamedRoutes.coursesPath(), CoursesController::showAll);

        // Отображение формы для добавления курса
        app.get(NamedRoutes.buildCoursePath(), CoursesController::build);

        // Обработка запросов на отображение страницы конкретного курса из списка
        app.get(NamedRoutes.coursePath("{id}"), CoursesController::show);

        // Обработка запросов на добавление нового курса
        app.post(NamedRoutes.coursesPath(), CoursesController::create);

        // Отображение формы редактирования данных курса
        app.get(NamedRoutes.courseEditPath("{id}"), CoursesController::editData);

        // Обработка запросов на обновление данных курса
        app.patch(NamedRoutes.coursePath("{id}"), CoursesController::update);

        // Обработка запросов на удаление данных курса
        app.delete(NamedRoutes.coursePath("{id}"), CoursesController::delete);

        // Обработка запросов на обновление данных и удаление курса
        app.post(NamedRoutes.coursePath("{id}"), ctx -> {
            String method = ctx.formParam("_method");

            if ("PATCH".equalsIgnoreCase(method)) {
                CoursesController.update(ctx);

                // Обработка запросов на удаление пользователя
            } else if ("DELETE".equalsIgnoreCase(method)) {
                CoursesController.delete(ctx);
            }
        });

        return app;
    }

    public static void main(String[] args) {
        Javalin app = getApp();

        var javaCourse = new Course("Java", "Этот курс научит вас программировать на Java");
        Long idJavaCourse = CourseRepository.save(javaCourse);

        var javaCourses = CourseRepository.search("Java");

        app.start(7070); // Стартуем веб-сервер
    }
}
