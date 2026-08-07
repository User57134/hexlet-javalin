package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.apache.commons.text.StringEscapeUtils;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.model.Course;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.repository.CourseRepository;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;


import java.util.ArrayList;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;
import static java.util.stream.Collectors.toList;

public class HelloWorld {

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

        // Описываем, что загрузится по адресу /
        app.get("/", ctx -> {
            ctx.render("index.jte");
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

            var courses = CourseRepository.getEntities();

            var course = courses.stream()
                    .filter(
                            c -> (c.getId().longValue() == id)
                    ).findFirst();

            if (course.isPresent()) {
                var page = new CoursePage(course.get());
                ctx.render("courses/selectedCourse.jte", model("page", page));
            }
        });

        app.get("/courses", ctx -> {
            final var term = ctx.queryParam("term");

            var courses = CourseRepository.getEntities();
            List<Course> resultList = new ArrayList<>();

            if (term != null) {
                resultList = courses.stream()
                        .filter(
                                course -> (
                                        course.getName().equals(term))
                        ).toList();

                if (resultList.isEmpty()) {
                    resultList = courses.stream()
                            .filter(
                                    course -> (
                                            course.getDescription().contains(term))
                            ).toList();
                }

            } else {
                resultList = courses;
            }

            var currentTerm = (term != null) ? term : courses.getFirst().getName();

            var page = new CoursesPage(resultList, "Доступные курсы", currentTerm);
            ctx.render("courses/index.jte", model("page", page));
        });

        app.start(7070); // Стартуем веб-сервер
    }
}
