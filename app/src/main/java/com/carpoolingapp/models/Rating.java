package com.carpoolingapp.models;
// File: CarpoolingApp/app/src/main/java/com/carpooling/app/models/Rating.java
public class Rating {
    private String ratingId;
    private String bookingId;
    private String rideId;
    private String fromUserId;
    private String fromUserName;
    private String toUserId;
    private String toUserName;
    private float rating;
    private String review;
    private long createdAt;

    // Default constructor required for Firebase
    public Rating() {
    }

    public Rating(String bookingId, String rideId, String fromUserId, String fromUserName,
                  String toUserId, String toUserName, float rating, String review) {
        this.bookingId = bookingId;
        this.rideId = rideId;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.rating = rating;
        this.review = review;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

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

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}