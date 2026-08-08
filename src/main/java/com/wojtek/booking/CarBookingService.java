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
    private final UserService userService = new UserService();
    private final CarService carService = new CarService();
    private final CarBookingDao carBookingDao = new CarBookingDao();

//    FR-01
    public CarBooking bookCar(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) {
        User user = userService.getUserById(userId);
        if (user == null) throw new NoSuchElementException("User not found!");

        Car car = carService.getCarById(carId);
        if (car == null) throw new NoSuchElementException("Car not found");

        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(startDate))
            throw new IllegalArgumentException("Wrong date");

        CarBooking[] carBookings = carBookingDao.getAllCarBookings();

        long days = getNumberOfDays(startDate, endDate);
        if(days == 0) {
            throw new IllegalArgumentException("You can't book a car for 0 days!");
        }

        BigDecimal totalPriceOfRental = getTotalRentalPrice(
                days,
                car.getRentalPricePerDay()
        );

        if (carBookings.length == 0) {
            CarBooking carBooking = new CarBooking(
                    user, car, startDate, endDate, totalPriceOfRental);
            carBookingDao.bookCar(carBooking);
            return carBooking;
        }

        for (CarBooking carBooking : carBookings) {
            if(isOverlapping(startDate, endDate, carBooking.getStartDate(), carBooking.getEndDate())) {
                if(carBooking.getCar().equals(car)) {
                    throw new IllegalArgumentException("Car is not available");
                }
            }
        }

        CarBooking carBooking = new CarBooking(
                user, car, startDate, endDate, totalPriceOfRental);
        carBookingDao.bookCar(carBooking);

        return carBooking;
    }

//    FR-02
    public boolean deleteBooking(UUID bookingId) {
        if (bookingId == null) return false;

        return carBookingDao.deleteBooking(bookingId);
    }

//    FR-03
    public CarBooking[] getAllBookingsOfSpecificUser(UUID userId) {
        CarBooking[] allBookings = carBookingDao.getAllCarBookings();

        int numberOfUserBookings = 0;

        for (CarBooking carBooking : allBookings) {
            if (carBooking.getUser().getId().equals(userId)) numberOfUserBookings++;
        }

        if (numberOfUserBookings == 0)
            return new CarBooking[0];

        int tmpIterator = 0;
        CarBooking[] allUserBookings = new CarBooking[numberOfUserBookings];
        for (int i = 0; i < allBookings.length; i++) {
            if (allBookings[i].getUser().getId().equals(userId)) {
                allUserBookings[tmpIterator++] = allBookings[i];
            }
        }
        return allUserBookings;
    }

//    FR-04
    public CarBooking[] getAllBookings() {
        return carBookingDao.getAllCarBookings();
    }

//    FR-05
    public Car[] getAllAvailableCars(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate)) throw new IllegalArgumentException("startDate cannot be after endDate!");
        if(startDate.isBefore(LocalDate.now())) throw new IllegalArgumentException("you cannot check availability in the past!");

        Car[] cars = carService.getAllCars();
        CarBooking[] allBookings = carBookingDao.getAllCarBookings();

        CarBooking[] allBookingsWithinTheDate =
                getAllBookingsWithinTheDate(allBookings, startDate, endDate);

        if (allBookingsWithinTheDate.length == 0) return cars;

        return getAvailableCars(allBookingsWithinTheDate, cars);
    }

//    FR-06
    public Car[] getAllElectricCars(LocalDate startDate, LocalDate endDate) {
        Car[] cars = getAllAvailableCars(startDate, endDate);

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

//    helper methods

    private boolean isOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private BigDecimal getTotalRentalPrice(long numberOfDays, BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(numberOfDays));
    }

    private long getNumberOfDays(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    private CarBooking[] getAllBookingsWithinTheDate(CarBooking[] allBookings, LocalDate startDate, LocalDate endDate) {
        int numberOfBookingsWhereDateMatch = 0;
        for (int i = 0; i < allBookings.length; i++) {
            LocalDate startDateOfBooking = allBookings[i].getStartDate();
            LocalDate endDateOfBooking = allBookings[i].getEndDate();

            if (isOverlapping(startDate, endDate, startDateOfBooking, endDateOfBooking)) {
                numberOfBookingsWhereDateMatch++;
            }
        }

        CarBooking[] allBookingsWithinTheDate = new CarBooking[numberOfBookingsWhereDateMatch];
        int tmpIterator = 0;

        for (int i = 0; i < allBookings.length; i++) {
            LocalDate startDateOfBooking = allBookings[i].getStartDate();
            LocalDate endDateOfBooking = allBookings[i].getEndDate();

            if (isOverlapping(startDate, endDate, startDateOfBooking, endDateOfBooking)) {
                allBookingsWithinTheDate[tmpIterator++] = allBookings[i];
            }
        }

        return allBookingsWithinTheDate;
    }

    private Car[] getAvailableCars(CarBooking[] allBookingsWithinTheDate, Car[] cars) {
        int numberOfAvailableCars = 0;

        for (Car car : cars) {
            boolean match = true;

            for (CarBooking carBooking : allBookingsWithinTheDate) {
                if (carBooking.getCar().equals(car)) {
                    match = false;
                    break;
                }
            }
            if(match) numberOfAvailableCars++;
        }

        Car[] availableCars = new Car[numberOfAvailableCars];
        int tmpIterator = 0;

        for (Car car : cars) {
            boolean match = true;

            for (CarBooking carBooking : allBookingsWithinTheDate) {
                if (carBooking.getCar().equals(car)) {
                    match = false;
                    break;
                }
            }
            if(match) availableCars[tmpIterator++] = car;
        }

        return availableCars;
    }
}
