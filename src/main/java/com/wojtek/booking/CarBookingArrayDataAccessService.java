package com.wojtek.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {
    private static List<CarBooking> carBookings;

    static {
        carBookings = new ArrayList<>();
    }

    @Override
    public List<CarBooking> getBookings() {
        return carBookings;
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        return carBookings.stream()
                .filter(carBooking -> carBooking.getId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean saveBooking(CarBooking request) {
        carBookings.add(request);
        return true;
    }

    @Override
    public boolean deleteBooking(UUID bookingId) {
        return carBookings.stream()
                .filter(carBooking -> carBooking.getId().equals(bookingId))
                .findFirst()
                .map(carBooking -> {
                    carBooking.setStatus(BookingStatus.CANCELLED);
                    return true;
                }).orElse(false);
    }
}
