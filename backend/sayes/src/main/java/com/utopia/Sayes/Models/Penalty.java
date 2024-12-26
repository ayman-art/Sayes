package com.utopia.Sayes.Models;

public class Penalty {
    private long driverId;
    private long lotId;
    private double penalty;

    public Penalty(long driverId, long lotId, double penalty) {
        this.driverId = driverId;
        this.lotId = lotId;
        this.penalty = penalty;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getLotId() {
        return lotId;
    }

    public void setLotId(long lotId) {
        this.lotId = lotId;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }
}
