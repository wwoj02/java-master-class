package com.wojtek.user;

import java.util.UUID;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

//    FR-07
    public User[] getUsers() {
        return userDao.getUsers();
    }

    public User getUserById(UUID id) {
        return userDao.getUserById(id);
    }

}
