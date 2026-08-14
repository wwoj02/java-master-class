package com.wojtek.car;

import java.util.UUID;

public interface CarDao {
    Car[] getCars();

    Car findCarById(UUID carId);
}
