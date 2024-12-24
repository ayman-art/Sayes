package com.utopia.Sayes.Modules.WebSocket;

import com.utopia.Sayes.DTOs.UpdateDriverReservationDTO;
import com.utopia.Sayes.DTOs.UpdateLotDTO;
import com.utopia.Sayes.DTOs.UpdateLotManagerLotSpotsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyLotUpdate(UpdateLotDTO updateLotDTO) {
        messagingTemplate.convertAndSend("/topic/lots-update", updateLotDTO);
    }

    public void notifyLotManager(UpdateLotManagerLotSpotsDTO updateLotManagerLotSpotsDTO) {
        messagingTemplate.convertAndSend(
                "/topic/lot-manager-update/" + updateLotManagerLotSpotsDTO.getLotId(),
                updateLotManagerLotSpotsDTO);
    }

    public void notifyDriverReservation(UpdateDriverReservationDTO updateDriverReservationDTO) {
        messagingTemplate.convertAndSend(
                "/topic/driver-reservation-update/" + updateDriverReservationDTO.getDriverId(),
                updateDriverReservationDTO);

    }

}
