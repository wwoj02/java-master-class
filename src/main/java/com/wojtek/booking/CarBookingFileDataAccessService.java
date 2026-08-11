package com.wojtek.booking;

import java.io.*;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao {
    private final String pathfile;

    public CarBookingFileDataAccessService(String pathfile) {
        this.pathfile = pathfile;

        File file = new File(pathfile);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(file.length() == 0) {
            updateFile(new CarBooking[0]);
        }
    }

    @Override
    public CarBooking[] getBookings() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(pathfile))) {
            return (CarBooking[]) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Couldn't retrieve the data from the file.");
        }
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        CarBooking[] bookings = getBookings();
        for (CarBooking booking : bookings) {
            if (booking.getId().equals(bookingId)) return booking;
        }
        return null;
    }

    @Override
    public boolean saveBooking(CarBooking request) {
        CarBooking[] bookings = getBookings();
        CarBooking[] newBookingsArr = new CarBooking[bookings.length + 1];
        for (int i = 0; i < bookings.length; i++) {
            newBookingsArr[i] = bookings[i];
        }
        newBookingsArr[newBookingsArr.length - 1] = request;

        return updateFile(newBookingsArr);
    }

    @Override
    public boolean deleteBooking(UUID bookingId) {
        CarBooking[] bookings = getBookings();

        boolean bookingExists = false;
        for (int i = 0; i < bookings.length; i++) {
            if (bookings[i].getId().equals(bookingId)) bookingExists = true;
        }

        if(!bookingExists) return false;

        CarBooking[] newBookingsArr = new CarBooking[bookings.length - 1];

        int iterator = 0;
        for (int i = 0; i < bookings.length; i++) {
            if(bookings[i].getId().equals(bookingId)) continue;
            newBookingsArr[iterator++] = bookings[i];
        }

        updateFile(newBookingsArr);
        return true;
    }

//    helper methods

    private boolean updateFile(CarBooking[] newBookingsArr) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathfile))) {
            objectOutputStream.writeObject(newBookingsArr);
            return true;
        }  catch (IOException e) {
            throw new RuntimeException("Couldn't save data to the file.", e);
        }
    }
}
