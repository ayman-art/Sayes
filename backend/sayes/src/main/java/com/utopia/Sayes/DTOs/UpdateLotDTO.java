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
public class UpdateLotDTO {
    private long lotId;
    private long noOfSpots;
    private double longitude;
    private double latitude;
    private double price;
    private String lot_type;


}
