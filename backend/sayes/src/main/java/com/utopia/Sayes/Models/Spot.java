package com.utopia.Sayes.Models;

public class Spot {
    private long spot_id;
    private long lot_id;
    private String state;

    public Spot(long spot_id, long lot_id, String state) {
        this.spot_id = spot_id;
        this.lot_id = lot_id;
        this.state = state;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
