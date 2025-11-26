package com.carpoolingapp.models;
// File: CarpoolingApp/app/src/main/java/com/carpooling/app/models/Booking.java
public class Booking {
    private String bookingId;
    private String rideId;
    private String riderId;
    private String riderName;
    private String driverId;
    private String driverName;
    private String fromLocation;
    private String toLocation;
    private String date;
    private String time;
    private int seatsBooked;
    private double totalPrice;
    private String paymentMethod;
    private String status; // "confirmed", "completed", "cancelled"
    private long createdAt;
    private long updatedAt;

    // Default constructor required for Firebase
    public Booking() {
    }

    public Booking(String rideId, String riderId, String riderName, String driverId,
                   String driverName, String fromLocation, String toLocation, String date,
                   String time, int seatsBooked, double totalPrice, String paymentMethod) {
        this.rideId = rideId;
        this.riderId = riderId;
        this.riderName = riderName;
        this.driverId = driverId;
        this.driverName = driverName;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.date = date;
        this.time = time;
        this.seatsBooked = seatsBooked;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.status = "confirmed";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}