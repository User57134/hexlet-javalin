package org.example.hexlet.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.example.hexlet.model.User;


public class UserRepository {
    @Getter
    private static List<User> entities = new ArrayList<User>();


    public static void save(User user) {
        user.setId((long) entities.size() + 1);
        user.setCreatedAt(LocalDateTime.now());
        entities.add(user);
    }


    public static List<User> search(String term) {
        return entities.stream()
                .filter(entity -> entity.getName().startsWith(term))
                .toList();
    }


    public static Optional<User> find(Long id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findAny();
    }


    public static boolean delete(Long id) {
        return entities.removeIf(user -> user.getId().equals(id));
    }


    public static void removeAll() {
        entities = new ArrayList<User>();
    }
}
