package org.example.hexlet.repository;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.example.hexlet.App;
import org.example.hexlet.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CourseRepository extends BaseRepository {
    private static final Logger log = LoggerFactory.getLogger(App.class);


    public static List<Course> getEntities() {
        List<Course> courses = new LinkedList<>();

        String sql = "SELECT * FROM courses";

        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();

            var resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                var id = resultSet.getLong(1);
                var name = resultSet.getString("name");
                var description = resultSet.getString("description");

                var course = new Course(id, name, description);

                courses.add(course);
            }

        } catch (SQLException ex) {
            log.error("CourseRepository::getEntities() error: {}", ex.getMessage());
        }

        return courses;
    }


    public static List<Course> search(String term) {
        List<Course> courses = new LinkedList<>();

        String sql = "SELECT * FROM courses WHERE courses.name ILIKE ?";

        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + term + "%");

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                var id = resultSet.getLong(1);
                var name = resultSet.getString("name");
                var description = resultSet.getString("description");

                var course = new Course(id, name, description);

                courses.add(course);
            }

        } catch (SQLException ex) {
            log.error("CourseRepository::search() error: {}", ex.getMessage());
        }

        return courses;
    }


    public static Long save(Course course) {
        String sql = "INSERT INTO courses (name, description) VALUES(?, ?)";
        Long id = null;

        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, course.getDescription());

            preparedStatement.executeUpdate();

            var generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                id = generatedKey.getLong(1);
                course.setId(id);
                return id;
            } else {
                log.error("DB has not returned an id after saving the course: " + course.getName());
                return 0L;
            }

        } catch (SQLException ex) {
            log.error("CourseRepository::save() error: {}", ex.getMessage());
            return 0L;
        }
    }


    public static Optional<Course> find(Long id) {
        String sql = "SELECT * FROM courses WHERE id = ?";

        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var description = resultSet.getString("description");

                if (name != null && description != null) {
                    return Optional.of(new Course(id, name, description));
                }
            }
        } catch (SQLException ex) {
            log.error("CourseRepository::find() error: {}", ex.getMessage());
        }

        return Optional.empty();
    }


    public static boolean delete(Long id) {
        var sql = "DELETE FROM courses WHERE courses.id = ?";

        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                return true;
            }
        } catch (SQLException ex) {
            log.error("CourseRepository::delete() error: {}", ex.getMessage());
        }

        return false;
    }


    public static int deleteAll() {
        var sql = "DELETE FROM courses";

        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();

            int rowsDeleted = statement.executeUpdate(sql);

            return rowsDeleted;

        } catch (SQLException ex) {
            log.error("CourseRepository::deleteAll() error: {}", ex.getMessage());
        }

        return 0;
    }
}