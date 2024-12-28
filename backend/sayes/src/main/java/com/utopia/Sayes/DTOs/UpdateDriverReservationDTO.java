package com.utopia.Sayes.DTOs;

import com.utopia.Sayes.enums.SpotStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDriverReservationDTO {
    private long driverId;
    private long lotId;
    private long spotId;
    private SpotStatus status;
    private double penalty;
    private double price;
    private Long remainingReservationTime;
    private Long remainingParkingTime;
}
