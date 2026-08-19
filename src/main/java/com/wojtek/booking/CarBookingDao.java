package com.wojtek.booking;

import java.util.List;
import java.util.UUID;

public interface CarBookingDao {
    List<CarBooking> getBookings();

    CarBooking findBookingById(UUID bookingId);

    boolean saveBooking(CarBooking request);

    boolean deleteBooking(UUID bookingId);
}
