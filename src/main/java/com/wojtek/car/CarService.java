package com.wojtek.car;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class CarService {
    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> getCars() {
        return carDao.getCars();
    }

    public Car findCarById(UUID carId) {
        return carDao.findCarById(carId)
                .orElseThrow(() -> new NoSuchElementException("Car not found!"));
    }
}
