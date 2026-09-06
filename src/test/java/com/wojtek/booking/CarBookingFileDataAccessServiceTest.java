package com.wojtek.booking;

import com.wojtek.car.Brand;
import com.wojtek.car.Car;
import com.wojtek.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarBookingFileDataAccessServiceTest {

    @TempDir
    Path tempDir;

    private CarBookingFileDataAccessService underTest;

    @BeforeEach
    void setUp() {
        var filePath = tempDir.resolve("bookings.dat");
        underTest = new CarBookingFileDataAccessService(filePath);
    }

    @Test
    void itShouldSaveAndRetrieveBooking() {
        // Given
        LocalDate localDate = LocalDate.now();
        User user = new User("Wojtek");

        Car car = new Car(
                "123456",
                BigDecimal.valueOf(50.00),
                Brand.MERCEDES,
                true);

        CarBooking carBooking = new CarBooking(
                user,
                car,
                localDate,
                localDate.plusDays(5),
                BigDecimal.valueOf(29.99),
                BookingStatus.ACTIVE);

        // When
        underTest.saveBooking(carBooking);
        List<CarBooking> bookings = underTest.getBookings();

        // Then
        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(carBooking.getId());
}
    @Test
    void itShouldDeleteBooking() {
        LocalDate localDate = LocalDate.now();
        User user = new User("Wojtek");

        Car car = new Car(
                "123456",
                BigDecimal.valueOf(50.00),
                Brand.MERCEDES,
                true);

        CarBooking carBooking = new CarBooking(
                user,
                car,
                localDate,
                localDate.plusDays(5),
                BigDecimal.valueOf(29.99),
                BookingStatus.ACTIVE);

        underTest.saveBooking(carBooking);
        underTest.deleteBooking(carBooking.getId());

        assertTrue(underTest.getBookings().isEmpty());
    }
}
