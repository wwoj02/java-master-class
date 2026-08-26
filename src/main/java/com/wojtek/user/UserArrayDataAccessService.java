package com.wojtek.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {
    private static final List<User> users;

    static {
        users = new ArrayList<>(List.of(
                new User("Wojtek"),
                new User("Josh"),
                new User("Thomas"),
                new User("Oscar")
                ));
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<User> getUserByName(String name) {
        return users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }
}
