package com.wojtek;

import com.wojtek.booking.CarBooking;
import com.wojtek.booking.CarBookingDao;
import com.wojtek.booking.CarBookingFileDataAccessService;
import com.wojtek.booking.CarBookingService;
import com.wojtek.car.CarFakerDataAccessService;
import com.wojtek.car.CarService;
import com.wojtek.user.User;
import com.wojtek.user.UserFakerDataAccessService;
import com.wojtek.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private final static UserService userService = new UserService(new UserFakerDataAccessService());
    private final static CarService carService = new CarService(new CarFakerDataAccessService());
    private final static CarBookingDao carBookingDao =
            new CarBookingFileDataAccessService("data.dat");
    private final static CarBookingService carBookingService =
            new CarBookingService(userService, carService, carBookingDao);
    private final static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {

        int choice = 0;

        while(choice != 8) {
            menu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("You can enter only the numbers within the range (1 - 8)");
                choice = 0;
            }
            switch (choice) {
                case 1 -> newBooking();
                case 2 -> deleteBooking();
                case 3 -> getUserBookings();
                case 4 -> getAllBookings();
                case 5 -> getAvailableCars();
                case 6 -> getElectricCars();
                case 7 -> viewAllUsers();
                case 8 -> System.out.println("Goodbye!");
            }
        }

        scanner.close();
    }

    static void menu() {
        System.out.println("==========================================");
        System.out.println("Car booking system! Choose action (1 - 8)");
        System.out.println("==========================================");

        System.out.println("(1) book a car");
        System.out.println("(2) delete booking");
        System.out.println("(3) view user bookings");
        System.out.println("(4) view all bookings");
        System.out.println("(5) view available cars");
        System.out.println("(6) view electric cars");
        System.out.println("(7) view all users");
        System.out.println("(8) exit");
    }

    static void viewAllUsers() {
        List<User> users = userService.getUsers();
        int counter = 1;
        for (User user : users) {
            System.out.println(String.format(
                    "%d) %s",
                    counter++, user
            ));
        }
    }

    static void getElectricCars() {
        System.out.println("All available electric cars: ");

        carBookingService.getAvailableElectricCars()
                .forEach(System.out::println);
    }

    static void getAvailableCars() {
        carBookingService.getAllAvailableCars()
                .forEach(System.out::println);
    }

    static void getAllBookings() {
        carBookingService.getAllBookings()
                .forEach(System.out::println);
    }

    static void getUserBookings() {
        System.out.print("Enter user id: (first view all the users then [copy & paste] here)");
        String userId = scanner.nextLine();

        carBookingService.getAllBookingsByUserId(UUID.fromString(userId))
                .forEach(System.out::println);
    }

    static void deleteBooking() {
        System.out.print("Enter booking id: (first view all bookings then [copy & paste] here)");
        String bookingId = scanner.nextLine();


        boolean deleted = carBookingService.deleteBooking(UUID.fromString(bookingId));
        if(deleted) System.out.println(String.format(
                "Deleted the booking id: %s now car is available again!", bookingId
        ));
    }

    static void newBooking() {
        System.out.print("Enter user id: (first view all users then [copy & paste] here): ");
        String userId = scanner.nextLine();

        System.out.print("Enter car id: (first view all available cars then [copy & paste] here): ");
        String carId = scanner.nextLine();

        System.out.println("Start Date");
        LocalDate startDate = getDate();

        System.out.println("End date");
        LocalDate endDate = getDate();

        CarBooking booking = carBookingService.bookCar(
                UUID.fromString(userId),
                UUID.fromString(carId),
                startDate,
                endDate
        );

        System.out.println("You've successfully booked a car! Car details below:");
        System.out.println(booking);
    }

//    helper methods
    private static LocalDate getDate() {
        System.out.print("Enter date: year month day (ex. 2020 5 23): ");
        String startDate = scanner.nextLine();

        String[] sD = startDate.split(" ");
        return LocalDate.of(
                Integer.parseInt(sD[0]),
                Integer.parseInt(sD[1]),
                Integer.parseInt(sD[2])
        );
    }
}
