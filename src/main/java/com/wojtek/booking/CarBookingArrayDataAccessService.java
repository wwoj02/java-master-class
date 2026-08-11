package com.wojtek.booking;

import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {
    private static CarBooking[] carBookings;
    private static int nextAvailableIndex;

    static {
        carBookings = new CarBooking[10];
        nextAvailableIndex = 0;
    }

    @Override
    public CarBooking[] getBookings() {
        return carBookings;
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        for (CarBooking booking : carBookings) {
            if(booking == null) break;
            if(booking.getId().equals(bookingId)) return booking;
        }
        return null;
    }

    @Override
    public CarBooking saveBooking(CarBooking request) {
        if (nextAvailableIndex == carBookings.length) {
            CarBooking[] newCarBookingsArray = new CarBooking[carBookings.length * 2];
            for (int i = 0; i < carBookings.length; i++) {
                newCarBookingsArray[i] = carBookings[i];
            }
            carBookings = newCarBookingsArray;
        }

        carBookings[nextAvailableIndex++] = request;
        return request;
    }

    @Override
    public boolean deleteBooking(UUID bookingId) {
        for (int i = 0; i < carBookings.length; i++) {
            if(carBookings[i] == null) break;
            if (carBookings[i].getId().equals(bookingId)) {
                carBookings[i].setStatus(BookingStatus.CANCELLED);
                return true;
            }
        }
        return false;
    }
}
