package com.wojtek.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserArrayDataAccessServiceTest {

    private UserArrayDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserArrayDataAccessService();
    }

    @Test
    void canGetAllUsers() {
        var actual = underTest.getUsers();
        assertFalse(actual.isEmpty());
    }

    @Test
    void canFindCarById() {
        User expected = underTest.getUsers().getFirst();

        var actual = underTest.findUserById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
