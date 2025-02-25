package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {

        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            throw new IllegalArgumentException("user id가 없습니다.");
        }

        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static void deleteAll() {
        users.clear();
    }
}
