package com.utopia.Sayes.Models;

import java.sql.Time;

public class Reservation {
    private long spot_id;
    private long lot_id;
    private Time reservation_time;
    private String state;
    private long driver_id;

    public Reservation(long spot_id, long lot_id, Time reservation_time, String state, long driver_id) {
        this.spot_id = spot_id;
        this.lot_id = lot_id;
        this.reservation_time = reservation_time;
        this.state = state;
        this.driver_id = driver_id;
    }

    public long getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(long spot_id) {
        this.spot_id = spot_id;
    }

    public long getLot_id() {
        return lot_id;
    }

    public void setLot_id(long lot_id) {
        this.lot_id = lot_id;
    }

    public Time getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(Time reservation_time) {
        this.reservation_time = reservation_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(long driver_id) {
        this.driver_id = driver_id;
    }
}
