package com.wojtek.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarArrayDataAccessServiceTest {
    private CarArrayDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CarArrayDataAccessService();
    }

    @Test
    void canGetAllCars() {
        var actual = underTest.getCars();
        assertFalse(actual.isEmpty());
    }

    @Test
    void canFindCarById() {
        Car expected = underTest.getCars().getFirst();

        var actual = underTest.findCarById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
