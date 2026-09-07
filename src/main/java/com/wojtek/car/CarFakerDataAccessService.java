package com.wojtek.car;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarFakerDataAccessService implements CarDao {
    private final Faker faker;
    private final List<Car> cars;

    public CarFakerDataAccessService() {
        faker = new Faker();
        cars = new ArrayList<>();
        Brand[] brands = Brand.values();

        for (int i = 0; i < 20; i++) {
            cars.add(new Car(
                    String.valueOf(faker.number().randomNumber(6, true)),
                    BigDecimal.valueOf(faker.number().randomDouble(4, 100, 1000)),
                    brands[faker.random().nextInt(brands.length)],
                    faker.random().nextBoolean()
            ));
        }
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
