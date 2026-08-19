package com.wojtek.user;

import java.util.ArrayList;
import java.util.List;
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
    public User findUserById(UUID id) {
        for (User user : users) {
            if(user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByName(String name) {
        for (User user : users) {
            if(user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }
}
