package com.wojtek.booking;

import com.wojtek.car.Car;
import com.wojtek.car.CarService;
import com.wojtek.exception.BookingNotFoundException;
import com.wojtek.exception.CarAlreadyBookedException;
import com.wojtek.exception.InvalidBookingDateException;
import com.wojtek.user.User;
import com.wojtek.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Car carFromDao = carService.findCarById(carId);

        if (startDate.isBefore(LocalDate.now()) || !endDate.isAfter(startDate))
            throw new InvalidBookingDateException("End date is after start date!");

        boolean isBooked = getAllAvailableCars().stream()
                .anyMatch(carFromDao::equals);

        if(!isBooked) throw new CarAlreadyBookedException("Car is already booked!");

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
        return carBookingDao.getBookings().stream()
                .filter(booking -> booking.getUser().getId().equals(userId))
                .toList();
    }

//    FR-04
    public List<CarBooking> getAllBookings() {
        return carBookingDao.getBookings();
    }

//    FR-05
    public List<Car> getAllAvailableCars() {
        List<CarBooking> allBookings = getAllBookings();
        return carService.getCars().stream()
                .filter(car -> allBookings.stream()
                        .noneMatch(carBooking ->
                                carBooking.getCar().equals(car) && carBooking.getStatus() == BookingStatus.ACTIVE))
                .collect(Collectors.toList());
    }

//    FR-06
    public List<Car> getAvailableElectricCars() {
        return getAllAvailableCars().stream()
                .filter(Car::isElectric)
                .collect(Collectors.toList());
    }

    public CarBooking findBookingById(UUID bookingId) {
        return carBookingDao.findBookingById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found!"));
    }

//    helper methods

    private BigDecimal getTotalRentalPrice(long numberOfDays, BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(numberOfDays));
    }

    private long getNumberOfDays(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
