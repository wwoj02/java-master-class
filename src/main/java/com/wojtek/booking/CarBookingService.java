package com.wojtek.booking;

import com.wojtek.car.Car;
import com.wojtek.car.CarService;
import com.wojtek.user.User;
import com.wojtek.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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

        List<Car> availableCars = getAllAvailableCars();

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
    public List<CarBooking> getAllBookingsByUserId(UUID userId) {
        List<CarBooking> allBookings = carBookingDao.getBookings();

        List<CarBooking> allUserBookings = new ArrayList<>();
        for (CarBooking carBooking : allBookings) {
            if (carBooking.getUser().getId().equals(userId)) {
                allUserBookings.add(carBooking);
            }
        }
        return allUserBookings;
    }

//    FR-04
    public List<CarBooking> getAllBookings() {
        return carBookingDao.getBookings();
    }

//    FR-05
    public List<Car> getAllAvailableCars() {

        List<Car> cars = carService.getCars();
        List<CarBooking> carBookings = carBookingDao.getBookings();

        List<Car> availableCarsArr = new ArrayList<>();

        for (Car car : cars) {
            boolean isBooked = false;
            for (CarBooking carBooking : carBookings) {
                if(carBooking.getCar().equals(car) && carBooking.getStatus().equals(BookingStatus.ACTIVE)) {
                    isBooked = true;
                    break;
                }
            }
            if(!isBooked) availableCarsArr.add(car);
        }

        return availableCarsArr;
    }

//    FR-06
    public List<Car> getAvailableElectricCars() {
        List<Car> cars = getAllAvailableCars();

        List<Car> electricCars = new ArrayList<>();
        for (Car car : cars) {
            if(car.isElectric()) electricCars.add(car);
        }

        return electricCars;
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
