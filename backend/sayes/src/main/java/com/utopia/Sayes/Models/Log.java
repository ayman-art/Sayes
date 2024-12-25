package com.utopia.Sayes.Models;

import java.time.LocalDateTime;

public class Log {
    private Long driverId;
    private LocalDateTime reservationTime;
    private LocalDateTime departureTime;
    private Long spotId;
    private Long lotId;

    public Log(Long driverId, LocalDateTime reservationTime, LocalDateTime departureTime, Long spotId, Long lotId) {
        this.driverId = driverId;
        this.reservationTime = reservationTime;
        this.departureTime = departureTime;
        this.spotId = spotId;
        this.lotId = lotId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }
}
