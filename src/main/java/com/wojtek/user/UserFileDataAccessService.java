package com.wojtek.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFileDataAccessService implements UserDao {
    private final String pathfile;

    public UserFileDataAccessService(String pathfile) {
        this.pathfile = pathfile;

        File file = new File(pathfile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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
                     new ObjectInputStream(new FileInputStream(pathfile))) {
            return (List<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Couldn't retrieve the data", e);
        }
    }

    @Override
    public User findUserById(UUID id) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User getUserByName(String name) {
        return getUsers().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void updateFile(List<User> users) {
        try (ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(pathfile))) {
            objectOutputStream.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save data to the file.", e);
        }
    }
}
