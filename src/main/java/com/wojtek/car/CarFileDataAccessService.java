package com.wojtek.car;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarFileDataAccessService implements CarDao {
    private final String pathfile;

    public CarFileDataAccessService(String pathfile) {
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
            updateFile(new ArrayList<>(List.of(
                    new Car("12345", new BigDecimal("12.99"), Brand.AUDI, true),
                    new Car("14566", new BigDecimal("9.99"), Brand.MERCEDES, false),
                    new Car("11234", new BigDecimal("7.99"), Brand.TOYOTA, true),
                    new Car("93821", new BigDecimal("18.99"), Brand.TESLA, false)
                    )));
        }
    }

    @Override
    public List<Car> getCars() {
        try (ObjectInputStream objectOutputStream =
                     new ObjectInputStream(new FileInputStream(pathfile))) {
            return (List<Car>) objectOutputStream.readObject();
        }  catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Couldn't retrieve data from the file.", e);
        }
    }

    @Override
    public Car findCarById(UUID carId) {
        return getCars().stream()
                .filter(car -> car.getId().equals(carId))
                .findFirst()
                .orElse(null);
    }

//    helper method
    private void updateFile(List<Car> cars) {
        try (ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(pathfile))) {
            objectOutputStream.writeObject(cars);
        }  catch (IOException e) {
            throw new RuntimeException("Couldn't save data to the file.", e);
        }
    }
}
