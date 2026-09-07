package com.wojtek.user;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserFakerDataAccessService implements UserDao {
    private final Faker faker;
    private final List<User> users;

    public UserFakerDataAccessService(){
        faker = new Faker();
        users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(new User(faker.name().fullName()));
        }
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

    public Optional<User> getUserByUsername(String name) {
        return users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }
}
