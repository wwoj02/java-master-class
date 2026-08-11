package com.wojtek.booking;

import java.util.UUID;

public interface CarBookingDao {
    CarBooking[] getBookings();

    CarBooking findBookingById(UUID bookingId);

    CarBooking saveBooking(CarBooking request);

    boolean deleteBooking(UUID bookingId);
}
