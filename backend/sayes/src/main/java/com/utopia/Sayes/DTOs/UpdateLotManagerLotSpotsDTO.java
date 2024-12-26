package com.utopia.Sayes.DTOs;

import com.utopia.Sayes.enums.SpotStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLotManagerLotSpotsDTO {
    private long spotId;
    private long lotId;
    private long lotManagerId;
    private long lotRevenue;
    private SpotStatus status;
}
