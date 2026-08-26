package com.wojtek.booking;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Optional<CarBooking> findBookingById(UUID bookingId) {
        return getBookings().stream()
                .filter(carBooking -> carBooking.getId().equals(bookingId))
                .findFirst();
    }

    @Override
    public boolean saveBooking(CarBooking request) {
        List<CarBooking> bookings = getBookings();
        bookings.add(request);

        return updateFile(bookings);
    }

    @Override
    public boolean deleteBooking(UUID bookingId) {
        List<CarBooking> allBookings = getBookings();
        List<CarBooking> bookings = allBookings.stream()
                .filter(booking -> !booking.getId().equals(bookingId))
                .collect(Collectors.toList());


        if (allBookings.size() == bookings.size()) return false;

        updateFile(bookings);
        return true;
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
