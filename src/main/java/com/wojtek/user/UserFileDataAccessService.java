package com.wojtek.user;

import com.wojtek.exception.FileDataAccessException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserFileDataAccessService implements UserDao {
    private final File file;

    public UserFileDataAccessService(String resourceName) {

        File file = new File(
                getClass().getClassLoader().getResource(resourceName).getPath());

        this.file = file;

        if (file.length() == 0) {
            updateFile(new ArrayList<>(List.of(
                    new User("Wojtek"),
                    new User("Josh"),
                    new User("Thomas"),
                    new User("Oscar")
            )));
        }
    }

    @Override
    public List<User> getUsers() {
        try (ObjectInputStream objectInputStream =
                     new ObjectInputStream(new FileInputStream(file.getPath()))) {
            return (List<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FileDataAccessException("Couldn't retrieve the data", e);
        }
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<User> getUserByName(String name) {
        return getUsers().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    private void updateFile(List<User> users) {
        try (ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(file.getPath()))) {
            objectOutputStream.writeObject(users);
        } catch (IOException e) {
            throw new FileDataAccessException("Couldn't save data to the file.", e);
        }
    }
}
