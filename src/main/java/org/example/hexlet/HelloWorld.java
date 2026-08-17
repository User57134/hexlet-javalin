package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.servlet.JavalinServletContext;
import io.javalin.rendering.template.JavalinJte;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.StringEscapeUtils;
import org.example.hexlet.controller.CoursesController;
import org.example.hexlet.controller.UsersController;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.model.Course;
import org.example.hexlet.repository.CourseRepository;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

import static io.javalin.rendering.template.TemplateUtil.model;


public class HelloWorld {
    private static final Logger log = LoggerFactory.getLogger(HelloWorld.class);

    public static void createCourses() {
        var javaCourse = new Course("Java", "Этот курс научит вас программировать на Java");
        CourseRepository.save(javaCourse);

        var pythonCourse = new Course("Python", "Этот курс научит вас программировать на Python");
        CourseRepository.save(pythonCourse);

        var phpCourse = new Course("PHP", "Этот курс научит вас программировать на PHP.");
        CourseRepository.save(phpCourse);

        var webCourse = new Course("Web", "Этот курс научит вас разрабатывать приложения для Web.");
        CourseRepository.save(webCourse);
    }

    public static void main(String[] args) {
        createCourses();

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
            var visited = Boolean.valueOf(ctx.cookie("visited"));
            var page = new MainPage(visited);
            ctx.render("index.jte", model("page", page));
            ctx.cookie("visited", String.valueOf(true));
        });

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

        app.start(7070); // Стартуем веб-сервер
    }
}
