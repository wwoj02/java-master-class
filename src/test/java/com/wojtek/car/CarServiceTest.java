package com.wojtek.car;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock CarDao carDao;
    @InjectMocks CarService carService;

    @Test
    void canGetAllCars() {
        List<Car> carList = mock();
        given(carDao.getCars()).willReturn(carList);

        var actual = carService.getCars();
        then(carDao).should().getCars();

        assertThat(actual).isSameAs(carList);
    }

    @Test
    void canFindCarById() {
        Car car = new Car(
                "123456",
                BigDecimal.valueOf(20.00),
                Brand.MERCEDES,
                true);

        given(carDao.findCarById(car.getId()))
                .willReturn(Optional.of(car));

        var actual = carService.findCarById(car.getId());

        then(carDao).should().findCarById(car.getId());

        assertThat(actual).isSameAs(car);
    }
}
