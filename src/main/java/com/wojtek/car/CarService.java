package com.wojtek.car;

import java.util.UUID;

public class CarService {
    private final CarDao carDao;

    public CarService() {
        carDao = new CarDao();
    }

    public Car[] getAllCars() {
        return carDao.getAllCars();
    }

    public Car getCarById(UUID carId) {
        return carDao.getCarById(carId);
    }
    public Car getCarByRegNumber(String regNumber) {
        return carDao.getCarByRegNumber(regNumber);
    }
}
