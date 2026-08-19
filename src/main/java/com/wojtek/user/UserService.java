package com.wojtek.user;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

//    FR-07
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User findUserById(UUID id) {
        return userDao.findUserById(id);
    }

}
