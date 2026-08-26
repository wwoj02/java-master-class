package com.wojtek.car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarArrayDataAccessService implements CarDao {
    private static final List<Car> cars;

    static {
        cars = new ArrayList<>(List.of(
                new Car("12345", new BigDecimal("12.99"), Brand.AUDI, true),
                new Car("14566", new BigDecimal("9.99"), Brand.MERCEDES, false),
                new Car("11234", new BigDecimal("7.99"), Brand.TOYOTA, true),
                new Car("93821", new BigDecimal("18.99"), Brand.TESLA, false)
                ));
    }

    @Override
    public List<Car> getCars() {
        return cars;
    }

    @Override
    public Optional<Car> findCarById(UUID carId) {
        return cars.stream()
                .filter(car -> car.getId().equals(carId))
                .findFirst();
    }
}
