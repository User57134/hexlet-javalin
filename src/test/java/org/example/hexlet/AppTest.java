package org.example.hexlet;

import io.javalin.testtools.JavalinTest;
import org.example.hexlet.model.Course;
import org.example.hexlet.repository.CourseRepository;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @Test
    void testCourseRepository() throws Exception {
        var app = App.getApp();

        // Тест добавления курсов
        var javaCourse = new Course("Java", "Этот курс научит вас программировать на Java");
        Long idJavaCourse = CourseRepository.save(javaCourse);
        assertThat(idJavaCourse).isGreaterThan(0);

        var pythonCourse = new Course("Python", "Этот курс научит вас программировать на Python");
        Long idPythonCourse = CourseRepository.save(pythonCourse);
        assertThat(idPythonCourse).isGreaterThan(0);

        var phpCourse = new Course("PHP", "Этот курс научит вас программировать на PHP.");
        Long idPhpCourse = CourseRepository.save(phpCourse);
        assertThat(idPhpCourse).isGreaterThan(0);

        var webCourse = new Course("Web", "Этот курс научит вас разрабатывать приложения для Web.");
        Long idWebCourse = CourseRepository.save(webCourse);
        assertThat(idWebCourse).isGreaterThan(0);

        // Тест на получение всех курсов, ожидаемое количество - 4
        var courses = CourseRepository.getEntities();
        assertThat(courses.size()).isEqualTo(4);

        // Тест на поиск курса по id
        {
            var course1 = CourseRepository.find(idJavaCourse);
            assertThat(course1.isPresent()).isEqualTo(true);

            var course2 = CourseRepository.find(idPythonCourse);
            assertThat(course2.isPresent()).isEqualTo(true);

            var course3 = CourseRepository.find(idPhpCourse);
            assertThat(course3.isPresent()).isEqualTo(true);

            var course4 = CourseRepository.find(idWebCourse);
            assertThat(course4.isPresent()).isEqualTo(true);
        }

        // удаление курсов
        assertThat(CourseRepository.delete(idJavaCourse)).isEqualTo(true);
        assertThat(CourseRepository.delete(idPythonCourse)).isEqualTo(true);
        assertThat(CourseRepository.delete(idPhpCourse)).isEqualTo(true);
        assertThat(CourseRepository.delete(idWebCourse)).isEqualTo(true);

        // тест отсутствия курсов после удаления
        assertThat(CourseRepository.getEntities().size()).isEqualTo(0);
        for (int i = 0; i < 4; i += 1) {
            var course = CourseRepository.find(courses.get(i).getId());
            assertThat(course.isPresent()).isEqualTo(false);
        }

        // добавление курос заново
        for (int i = 0; i < 4; i += 1) {
            CourseRepository.save(courses.get(i));
        }

        assertThat(CourseRepository.getEntities().size()).isEqualTo(4);

        // тест на поиск
        var javaCourses = CourseRepository.search("Java");
        assertThat(javaCourses.size()).isEqualTo(1);

        var phpCourses = CourseRepository.search("php");
        assertThat(phpCourses.size()).isEqualTo(1);

        var pythonCourses = CourseRepository.search("pYthOn");
        assertThat(pythonCourses.size()).isEqualTo(1);

        var webCourses = CourseRepository.search("web");
        assertThat(webCourses.size()).isEqualTo(1);

        var basicCourses = CourseRepository.search("Basic");
        assertThat(basicCourses.size()).isEqualTo(0);

        // удаление всех курсов
        int deleteAllResult = CourseRepository.deleteAll();
        assertThat(deleteAllResult).isGreaterThan(0);

        // тест отсутствия курсов после удаления
        assertThat(CourseRepository.getEntities().size()).isEqualTo(0);
        for (int i = 0; i < 4; i += 1) {
            var course = CourseRepository.find(courses.get(i).getId());
            assertThat(course.isPresent()).isEqualTo(false);
        }

        // добавление курос заново
        for (int i = 0; i < 4; i += 1) {
            CourseRepository.save(courses.get(i));
        }

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/courses");
            assertThat(response.code()).isEqualTo(200);

            var body = response.body().string();
            assertThat(body).contains("Java");
            assertThat(body).contains("PHP");
            assertThat(body).contains("Python");
            assertThat(body).contains("Web");
            assertThat(body).doesNotContain("Basic");
        });
    }
}
