package com.utopia.Sayes.Controllers;

import com.utopia.Sayes.Facades.ReservationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationFacade reservationFacade;
    @PutMapping("/reserve-spot")
    public ResponseEntity<?> reserveSpot(@RequestBody Map<String, Object> spotData , @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            spotData.put("jwt", token);
            Map<String , Object> response = reservationFacade.reserveSpot(spotData);
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/use-spot")
    public ResponseEntity<?> useSpot(@RequestBody Map<String, Object> spotData , @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            spotData.put("jwt", token);
            reservationFacade.useSpot(spotData);
            return new ResponseEntity<>("spot Occupied successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/free-spot")
    public ResponseEntity<?> freeSpot(@RequestBody Map<String, Object> spotData , @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            spotData.put("jwt", token);
            reservationFacade.freeSpot(spotData);
            return new ResponseEntity<>("spot freed successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
