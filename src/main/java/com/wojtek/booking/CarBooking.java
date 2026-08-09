package com.wojtek.booking;

import com.wojtek.car.Car;
import com.wojtek.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class CarBooking {
    private final UUID id;
    private User user;
    private Car car;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private BookingStatus status;
    private final LocalDate bookedAt;

    public CarBooking(User user, Car car, LocalDate startDate, LocalDate endDate, BigDecimal price, BookingStatus status) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status;
        this.bookedAt = LocalDate.now();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDate getBookedAt() {
        return bookedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CarBooking that = (CarBooking) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(car, that.car) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(price, that.price) && status == that.status && Objects.equals(bookedAt, that.bookedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, car, startDate, endDate, price, status, bookedAt);
    }

    @Override
    public String toString() {
        return "CarBooking{" +
                "id=" + id +
                ", user=" + user +
                ", car=" + car +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", status=" + status +
                ", bookedAt=" + bookedAt +
                '}';
    }
}
