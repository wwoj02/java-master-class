package com.wojtek.booking;

import com.wojtek.car.Car;
import com.wojtek.car.CarService;
import com.wojtek.user.User;
import com.wojtek.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.UUID;

public class CarBookingService {
    private final UserService userService;
    private final CarService carService;
    private final CarBookingDao carBookingDao;

    public CarBookingService(UserService userService, CarService carService, CarBookingDao carBookingDao) {
        this.userService = userService;
        this.carService = carService;
        this.carBookingDao = carBookingDao;
    }

    //    FR-01
    public CarBooking bookCar(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) {
        User user = userService.findUserById(userId);
        if (user == null) throw new NoSuchElementException("User not found!");

        Car carFromDao = carService.findCarById(carId);
        if (carFromDao == null) throw new NoSuchElementException("Car not found");

        if (startDate.isBefore(LocalDate.now()) || !endDate.isAfter(startDate))
            throw new IllegalArgumentException("Wrong date");

        Car[] availableCars = getAllAvailableCars();

        boolean isBooked = true;
        for (Car car : availableCars) {
            if (carFromDao.equals(car)) {
                isBooked = false;
                break;
            }
        }

        if(isBooked) throw new RuntimeException("Car is already booked!");

        long days = getNumberOfDays(startDate, endDate);

        BigDecimal totalPriceOfRental = getTotalRentalPrice(
                days,
                carFromDao.getRentalPricePerDay()
        );

        CarBooking carBooking = new CarBooking(
                user, carFromDao, startDate, endDate, totalPriceOfRental, BookingStatus.ACTIVE);
        carBookingDao.saveBooking(carBooking);

        return carBooking;
    }

//    FR-02
    public boolean deleteBooking(UUID bookingId) {
        if (bookingId == null) return false;

        return carBookingDao.deleteBooking(bookingId);
    }

//    FR-03
    public CarBooking[] getAllBookingsByUserId(UUID userId) {
        CarBooking[] allBookings = carBookingDao.getBookings();

        int numberOfUserBookings = 0;

        for (CarBooking carBooking : allBookings) {
            if (carBooking == null) break;
            if (carBooking.getUser().getId().equals(userId)) numberOfUserBookings++;
        }

        if (numberOfUserBookings == 0)
            return new CarBooking[0];

        int tmpIterator = 0;
        CarBooking[] allUserBookings = new CarBooking[numberOfUserBookings];
        for (int i = 0; i < allBookings.length; i++) {
            if(allBookings[i] == null) break;
            if (allBookings[i].getUser().getId().equals(userId)) {
                allUserBookings[tmpIterator++] = allBookings[i];
            }
        }
        return allUserBookings;
    }

//    FR-04
    public CarBooking[] getAllBookings() {
        return carBookingDao.getBookings();
    }

//    FR-05
    public Car[] getAllAvailableCars() {

        Car[] cars = carService.getCars();
        CarBooking[] carBookings = carBookingDao.getBookings();

        int numberOfAvailableCars = cars.length;

        for (Car car : cars)  {
            for (CarBooking carBooking : carBookings) {
                if (carBooking == null) break;
                if(carBooking.getCar().equals(car) && carBooking.getStatus().equals(BookingStatus.ACTIVE)) {
                    numberOfAvailableCars--;
                    break;
                }
            }
        }

        Car[] availableCarsArr = new Car[numberOfAvailableCars];
        int tmpIterator = 0;
        for (Car car : cars) {
            boolean isBooked = false;
            for (CarBooking carBooking : carBookings) {
                if(carBooking == null) break;
                if(carBooking.getCar().equals(car) && carBooking.getStatus().equals(BookingStatus.ACTIVE)) {
                    isBooked = true;
                    break;
                }
            }
            if(!isBooked) availableCarsArr[tmpIterator++] = car;
            if (tmpIterator == numberOfAvailableCars) break;
        }

        return availableCarsArr;
    }

//    FR-06
    public Car[] getAvailableElectricCars() {
        Car[] cars = getAllAvailableCars();

        int numberOfElectricCars = 0;
        for (Car car : cars) {
            if(car.isElectric()) numberOfElectricCars++;
        }

        Car[] availableElectricCars = new Car[numberOfElectricCars];

        int tmpIterator = 0;
        for (Car car : cars) {
            if(car.isElectric()) availableElectricCars[tmpIterator++] = car;
        }

        return availableElectricCars;
    }

    public CarBooking findBookingById(UUID bookingId) {
        return carBookingDao.findBookingById(bookingId);
    }

//    helper methods

    private BigDecimal getTotalRentalPrice(long numberOfDays, BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(numberOfDays));
    }

    private long getNumberOfDays(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
