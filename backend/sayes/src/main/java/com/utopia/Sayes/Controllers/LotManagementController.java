package com.utopia.Sayes.Controllers;

import com.utopia.Sayes.Facades.LotManagementFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin()
@RestController
@RequestMapping("/lot-management")
public class LotManagementController {
    @Autowired
    LotManagementFacade lotManagementFacade;
    @PostMapping("/create-lot")
    public ResponseEntity<?> createLot(@RequestBody Map<String, Object> lotData , @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            lotData.put("jwt", token);
            System.out.println(token);
            Map<String , Object> response = lotManagementFacade.createLot(lotData);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/add-spots")
    public ResponseEntity<?> addSpots(@RequestBody Map<String, Object> spotData , @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            spotData.put("jwt", token);
            lotManagementFacade.addSpot(spotData);
            return new ResponseEntity<>("spots added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-lots")
    public ResponseEntity<?> getLots(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Map<String , Object> response = lotManagementFacade.getLots(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
