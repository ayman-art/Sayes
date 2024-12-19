package com.utopia.Sayes.Models;

import java.time.Duration;

public class Lot {
    private long lot_id;
    private long manager_id;
    private double longitude;

    private double latitude;
    private long revenue;
    private double price;
    private long num_of_spots;

    private String lot_type;

    private double penalty;
    private double fee;
    private Duration time;
    private String details;

    public Lot(long manager_id, double longitude, double latitude,long revenue, double price, long num_of_spots, String lot_type
    , double penalty , double fee,Duration time) {
        this.manager_id = manager_id;
        this.price = price;
        this.num_of_spots = num_of_spots;
        this.longitude = longitude;
        this.latitude = latitude;
        this.revenue = revenue;
        this.lot_type = lot_type;
        this.penalty = penalty;
        this.fee = fee;
        this.time = time;
    }

    public String getLot_type() {
        return lot_type;
    }

    public void setLot_type(String lot_type) {
        this.lot_type = lot_type;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public long getLot_id() {
        return lot_id;
    }

    public void setLot_id(long lot_id) {
        this.lot_id = lot_id;
    }

    public long getManager_id() {
        return manager_id;
    }

    public void setManager_id(long manager_id) {
        this.manager_id = manager_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getNum_of_spots() {
        return num_of_spots;
    }

    public void setNum_of_spots(long num_of_spots) {
        this.num_of_spots = num_of_spots;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
