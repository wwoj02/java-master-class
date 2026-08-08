package com.wojtek.booking;

import java.util.UUID;

public class CarBookingDao {
    private static CarBooking[] carBookings;
    private static int nextAvailableIndex;

    static {
        carBookings = new CarBooking[0];
        nextAvailableIndex = 0;
    }

    public CarBooking[] getAllCarBookings() {
        return carBookings;
    }

    public CarBooking bookCar(CarBooking request) {
        if (nextAvailableIndex == carBookings.length) {
            CarBooking[] newCarBookingsArray = new CarBooking[carBookings.length + 1];
            for (int i = 0; i < carBookings.length; i++) {
                newCarBookingsArray[i] = carBookings[i];
            }
            carBookings = newCarBookingsArray;
        }

        carBookings[nextAvailableIndex++] = request;
        return request;
    }

    public boolean deleteBooking(UUID bookingId) {
        boolean found = false;
        for (int i = 0; i < carBookings.length; i++) {
            if (carBookings[i].getId().equals(bookingId)) {
                carBookings[i] = null;
                found = true;
                break;
            }
        }

        if(!found) return false;

        CarBooking[] newCarBookingsArray = new CarBooking[carBookings.length - 1];

        int tmpIterator = 0;
        for (CarBooking carBooking : carBookings) {
            if (carBooking != null && tmpIterator < newCarBookingsArray.length) {
                newCarBookingsArray[tmpIterator++] = carBooking;
            }
        }

        carBookings = newCarBookingsArray;
        nextAvailableIndex--;

        return true;
    }
}
