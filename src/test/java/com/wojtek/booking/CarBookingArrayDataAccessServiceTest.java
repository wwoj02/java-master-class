package com.wojtek.booking;

import com.wojtek.car.Brand;
import com.wojtek.car.Car;
import com.wojtek.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CarBookingArrayDataAccessServiceTest {

    private final CarBookingArrayDataAccessService underTest = new CarBookingArrayDataAccessService();
    LocalDate localDate = LocalDate.now();

    private final User user = new User("Wojtek");

    private final Car car = new Car(
            "123456",
            BigDecimal.valueOf(50.00),
            Brand.MERCEDES,
            true);

    private final CarBooking carBooking = new CarBooking(
            user,
            car,
            localDate,
            localDate.plusDays(5),
            BigDecimal.valueOf(29.99),
            BookingStatus.ACTIVE);

    @AfterEach
    void tearDown() {
        underTest.getBookings().clear();
    }

    @Test
    void canSaveBooking() {
        var actual = underTest.saveBooking(carBooking);
        var bookings = underTest.getBookings();

        assertAll(
                () -> assertTrue(actual),
                () -> assertTrue(bookings.contains(carBooking))
        );
    }

    @Test
    void canDeleteBooking() {
        underTest.saveBooking(carBooking);

        var actual = underTest.deleteBooking(carBooking.getId());

        assertAll(
                () -> assertTrue(actual),
                () -> assertThat(carBooking.getStatus()).isEqualTo(BookingStatus.CANCELLED));
    }

    @Test
    void canGetAllBookings() {
        underTest.saveBooking(carBooking);

        var actual = underTest.getBookings();

        var expected = List.of(carBooking);
        assertEquals(expected, actual);
    }

    @Test
    void canFindCarBookingById() {
        underTest.saveBooking(carBooking);

        var actual = underTest.findBookingById(carBooking.getId());

        assertTrue(actual.isPresent());
        assertEquals(carBooking, actual.get());
    }
}
