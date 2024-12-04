package com.utopia.Sayes.Models;

public class Lot {
    private long lot_id;
    private long manager_id;
    private String location;
    private long price;
    private long num_of_spots;
    private String details;

    public Lot(long manager_id, String location, long price, long num_of_spots) {
        this.manager_id = manager_id;
        this.location = location;
        this.price = price;
        this.num_of_spots = num_of_spots;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
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
