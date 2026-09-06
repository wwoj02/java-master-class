package com.wojtek.booking;

import com.wojtek.car.Brand;
import com.wojtek.car.Car;
import com.wojtek.car.CarService;
import com.wojtek.user.User;
import com.wojtek.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CarBookingServiceTest {

    @Mock private CarService carService;
    @Mock private UserService userService;
    @Mock private CarBookingDao carBookingDao;
    @InjectMocks private CarBookingService carBookingService;

    @Test
    void canBookCarCorrectly() {
        User user = new User("Wojtek");
        Car car = new Car(
                "123456",
                BigDecimal.valueOf(50.00),
                Brand.MERCEDES,
                true);
        LocalDate localDate = LocalDate.now();

        given(userService.findUserById(user.getId())).willReturn(user);
        given(carService.findCarById(car.getId())).willReturn(car);
        given(carBookingDao.getBookings()).willReturn(Collections.emptyList());
        given(carService.getCars()).willReturn(List.of(car));

        CarBooking actual = carBookingService.bookCar(
                user.getId(),
                car.getId(),
                localDate,
                localDate.plusDays(5));

        then(userService).should().findUserById(user.getId());
        then(carService).should().findCarById(car.getId());
        then(carBookingDao).should().getBookings();
        then(carBookingDao).should().saveBooking(actual);

        assertThat(actual.getUser()).isEqualTo(user);
        assertThat(actual.getCar()).isEqualTo(car);
        assertTrue(actual.getBookedAt().isBefore(actual.getEndDate()));
    }

    @Test
    void canDeleteBooking() {
        UUID id = UUID.randomUUID();
        given(carBookingDao.deleteBooking(id)).willReturn(true);

        boolean actual = carBookingService.deleteBooking(id);

        then(carBookingDao).should().deleteBooking(id);
        assertTrue(actual);
    }

    @Test
    void canGetBookings() {
        List<CarBooking> bookings = mock();
        given(carBookingDao.getBookings()).willReturn(bookings);

        var actual = carBookingService.getAllBookings();

        then(carBookingDao).should().getBookings();
        assertThat(actual).isSameAs(bookings);
    }

    @Test
    void canGetBookingById() {
        User user = new User("Wojtek");
        Car car = new Car(
                "123456",
                BigDecimal.valueOf(50.00),
                Brand.MERCEDES,
                true);
        LocalDate localDate = LocalDate.now();
        CarBooking carBooking = new CarBooking(
                user,
                car,
                localDate,
                localDate.plusDays(5),
                BigDecimal.valueOf(29.99),
                BookingStatus.ACTIVE);

        given(carBookingDao.findBookingById(carBooking.getId()))
                .willReturn(Optional.of(carBooking));

        var actual = carBookingService.findBookingById(carBooking.getId());

        then(carBookingDao)
                .should().findBookingById(carBooking.getId());
        assertThat(actual).isEqualTo(carBooking);
    }

//    GET ELECTRIC CARS TEST MOVED FROM CarServiceTest here

    @Test
    void canGetElectricCars() {

        List<Car> cars = List.of(
                new Car(
                        "123456",
                        BigDecimal.valueOf(25.00),
                        Brand.TOYOTA,
                        true),
                new Car(
                        "984553",
                        BigDecimal.valueOf(19.99),
                        Brand.MERCEDES,
                        false),
                new Car(
                        "166416",
                        BigDecimal.valueOf(23.00),
                        Brand.AUDI,
                        true));

        given(carService.getCars()).willReturn(cars);
        given(carBookingDao.getBookings()).willReturn(List.of());

        var actual = carBookingService.getAvailableElectricCars();

        List<Car> expected = new ArrayList<>(
                List.of(cars.getFirst(), cars.getLast()));

        then(carBookingDao).should().getBookings();
        then(carService).should().getCars();

        assertThat(actual).isEqualTo(expected);
    }
}
