package com.wojtek.booking;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
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
            updateFile(new ArrayList<>());
        }
    }

    @Override
    public List<CarBooking> getBookings() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(pathfile))) {
            return (List<CarBooking>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Couldn't retrieve the data from the file.");
        }
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        List<CarBooking> bookings = getBookings();
        for (CarBooking booking : bookings) {
            if (booking.getId().equals(bookingId)) return booking;
        }
        return null;
    }

    @Override
    public boolean saveBooking(CarBooking request) {
        List<CarBooking> bookings = getBookings();
        bookings.add(request);

        return updateFile(bookings);
    }

    @Override
    public boolean deleteBooking(UUID bookingId) {
        List<CarBooking> bookings = getBookings();

        for (CarBooking carBooking : bookings) {
            if(carBooking.getId().equals(bookingId)) {
                bookings.remove(carBooking);
                updateFile(bookings);
                return true;
            }
        }
        return false;
    }

//    helper methods

    private boolean updateFile(List<CarBooking> newBookingsArr) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathfile))) {
            objectOutputStream.writeObject(newBookingsArr);
            return true;
        }  catch (IOException e) {
            throw new RuntimeException("Couldn't save data to the file.", e);
        }
    }
}
