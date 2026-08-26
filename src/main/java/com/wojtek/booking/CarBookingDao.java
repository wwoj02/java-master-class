package com.wojtek.booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarBookingDao {
    List<CarBooking> getBookings();

    Optional<CarBooking> findBookingById(UUID bookingId);

    boolean saveBooking(CarBooking request);

    boolean deleteBooking(UUID bookingId);
}
