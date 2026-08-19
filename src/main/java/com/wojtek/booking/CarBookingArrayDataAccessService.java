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
        for (CarBooking carBooking : carBookings) {
            if(carBooking.getId().equals(bookingId)) return carBooking;
        }
        return null;
    }

    @Override
    public boolean saveBooking(CarBooking request) {
        carBookings.add(request);
        return true;
    }

    @Override
    public boolean deleteBooking(UUID bookingId) {
        for (CarBooking carBooking : carBookings) {
            if (carBooking.getId().equals(bookingId)) {
                carBookings.remove(carBooking);
                return true;
            }
        }
        return false;
    }
}
