package com.wojtek.car;

import java.math.BigDecimal;
import java.util.UUID;

public class CarDao {
    private static final Car[] cars;

    static {
        cars = new Car[] {
                new Car("12345", new BigDecimal("12.99"), Brand.AUDI, true),
                new Car("14566", new BigDecimal("9.99"), Brand.MERCEDES, false),
                new Car("11234", new BigDecimal("7.99"), Brand.TOYOTA, true),
                new Car("93821", new BigDecimal("18.99"), Brand.TESLA, false),
        };
    }

    public Car[] getAllCars() {
        return cars;
    }

    public Car getCarById(UUID carId) {
        for (Car car : cars) {
            if(car.getId().equals(carId)) {
                return car;
            }
        }
        throw new RuntimeException("Couldn't find a car!");
    }
    public Car getCarByRegNumber(String regNumber) {
        for (Car car : cars) {
            if(car.getRegNumber().equals(regNumber)) {
                return car;
            }
        }
        throw new RuntimeException("Couldn't find a car!");
    }

}
