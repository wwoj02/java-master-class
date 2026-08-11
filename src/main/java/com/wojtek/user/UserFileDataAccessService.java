package com.wojtek.user;

import java.io.*;
import java.util.UUID;

public class UserFileDataAccessService implements UserDao {
    private final String pathfile;

    public UserFileDataAccessService(String pathfile) {
        this.pathfile = pathfile;

        File file = new File(pathfile);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(file.length() == 0) {
            updateFile(new User[]{
                    new User("Wojtek"),
                    new User("Josh"),
                    new User("Thomas"),
                    new User("Oscar"),
            });
        }
    }

    @Override
    public User[] getUsers() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(pathfile))) {
            return (User[]) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Couldn't retrieve the data", e);
        }
    }

    @Override
    public User findUserById(UUID id) {
        for (User user : getUsers()) {
            if(user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByName(String name) {
        for (User user : getUsers()) {
            if(user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    private void updateFile(User[] users) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathfile))) {
            objectOutputStream.writeObject(users);
        }  catch (IOException e) {
            throw new RuntimeException("Couldn't save data to the file.", e);
        }
    }
}
