package org.example.hexlet.repository;

import org.example.hexlet.App;
import org.example.hexlet.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class UserRepository extends BaseRepository {
    private static final Logger log = LoggerFactory.getLogger(App.class);


    public static List<User> getEntities() {
        List<User> users = new LinkedList<>();

        String sql = "SELECT * FROM users";

        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();

            var resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                var id = resultSet.getLong(1);
                var name = resultSet.getString("name");
                var email = resultSet.getString("email");
                var password = resultSet.getString("password");

                var user = new User(id, name, email, password);

                users.add(user);
            }

        } catch (SQLException ex) {
            log.error("UserRepository::getEntities() error: {}", ex.getMessage());
        }

        return users;
    }


    public static List<User> search(String term) {
        List<User> users = new LinkedList<>();

        String sql = "SELECT * FROM users WHERE users.name LIKE '%?%'";

        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, term);

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                var id = resultSet.getLong(1);
                var name = resultSet.getString("name");
                var email = resultSet.getString("email");
                var password = resultSet.getString("password");

                var user = new User(id, name, email, password);

                users.add(user);
            }

        } catch (SQLException ex) {
            log.error("UserRepository::search() error: {}", ex.getMessage());
        }

        return users;
    }


    public static Long save(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES(?, ?, ?)";
        Long id = null;

        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());

            preparedStatement.executeUpdate();

            var generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                id = generatedKey.getLong(1);
                user.setId(id);
                return id;
            } else {
                log.error("DB has not returned an id after saving the user: " + user.getName());
                return 0L;
            }

        } catch (SQLException ex) {
            log.error("UserRepository::save() error: {}", ex.getMessage());
            return 0L;
        }
    }


    public static Optional<User> find(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var email = resultSet.getString("email");
                var password = resultSet.getString("password");

                if (name != null && email != null && password != null) {
                    return Optional.of(new User(id, name, email, password));
                }
            }
        } catch (SQLException ex) {
            log.error("UserRepository::find() error: {}", ex.getMessage());
        }

        return Optional.empty();
    }


    public static boolean delete(Long id) {
        var sql = "DELETE FROM users WHERE users.id = ?";

        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                return true;
            }
        } catch (SQLException ex) {
            log.error("UserRepository::delete() error: {}", ex.getMessage());
        }

        return false;
    }


    public static int deleteAll() {
        var sql = "DELETE FROM users";

        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();

            int rowsDeleted = statement.executeUpdate(sql);

            return rowsDeleted;

        } catch (SQLException ex) {
            log.error("userRepository::deleteAll() error: {}", ex.getMessage());
        }

        return 0;
    }
}
