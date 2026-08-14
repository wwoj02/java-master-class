package com.wojtek.user;

import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {
    private static final User[] users;

    static {
        users = new User[]{
                new User("Wojtek"),
                new User("Josh"),
                new User("Thomas"),
                new User("Oscar"),
        };
    }

    @Override
    public User[] getUsers() {
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
