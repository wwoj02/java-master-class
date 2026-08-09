package com.wojtek;

import com.wojtek.booking.CarBooking;
import com.wojtek.booking.CarBookingService;
import com.wojtek.car.Car;
import com.wojtek.car.CarService;
import com.wojtek.user.User;
import com.wojtek.user.UserService;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    final static UserService userService = new UserService();
    final static CarService carService = new CarService();
    final static CarBookingService carBookingService = new CarBookingService();

    public static void main(String[] args) {

        int choice = 0;
        Scanner scanner = new Scanner(System.in);

        while(choice != 8) {
            menu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("You can enter only the numbers within the range (1 - 8)");
                choice = 0;
            }
            switch (choice) {
                case 1 -> newBooking(scanner);
                case 2 -> deleteBooking(scanner);
                case 3 -> getUserBookings(scanner);
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
        User[] users = userService.getUsers();
        for (int i = 0; i < users.length; i++) {
            System.out.println(String.format(
                    "%d) %s",
                    i+1, users[i]
            ));
        }
    }

    static void getElectricCars() {
        Car[] electricCars = carBookingService.getAvailableElectricCars();

        System.out.println("All available electric cars: ");
        for (Car car : electricCars) {
            System.out.println(car);
        }
    }

    static void getAvailableCars() {
        Car[] availableCars = carBookingService.getAllAvailableCars();

        for (Car car : availableCars) {
            System.out.println(car);
        }
    }

    static void getAllBookings() {
        CarBooking[] bookings = carBookingService.getAllBookings();
        for (CarBooking booking : bookings) {
            if(booking == null) break;
            System.out.println(booking);
        }
    }

    static void getUserBookings(Scanner scanner) {
        System.out.print("Enter user id: (first view all the users then [copy & paste] here)");
        String userId = scanner.nextLine();
        CarBooking[] userBookings = carBookingService.getAllBookingsByUserId(UUID.fromString(userId));
        for (CarBooking booking : userBookings) {
            System.out.println(booking);
        }
    }

    static void deleteBooking(Scanner scanner) {
        System.out.print("Enter booking id: (first view all bookings then [copy & paste] here)");
        String bookingId = scanner.nextLine();


        boolean deleted = carBookingService.deleteBooking(UUID.fromString(bookingId));
        if(deleted) System.out.println(String.format(
                "Deleted the booking id: %s now car is available again!", bookingId
        ));
    }

    static void newBooking(Scanner scanner) {
        System.out.print("Enter user id: (first view all users then [copy & paste] here): ");
        String userId = scanner.nextLine();

        System.out.print("Enter car id: (first view all available cars then [copy & paste] here): ");
        String carId = scanner.nextLine();

        System.out.println("Start Date");
        LocalDate startDate = getDate(scanner);

        System.out.println("End date");
        LocalDate endDate = getDate(scanner);

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
    private static LocalDate getDate(Scanner scanner) {
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
