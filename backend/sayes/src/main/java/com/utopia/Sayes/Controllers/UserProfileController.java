package com.utopia.Sayes.Controllers;

import com.utopia.Sayes.Facades.ProfileFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin()
@RestController
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    ProfileFacade profileFacade;

    @GetMapping("/get-manager")
    public ResponseEntity<?> getManager(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Map<String , Object> response = profileFacade.getManagerData(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-driver")
    public ResponseEntity<?> getDriver(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("111");
            System.out.println(token);
            token = token.replace("Bearer ", "");
            Map<String , Object> response = profileFacade.getDriverData(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-admin")
    public ResponseEntity<?> getAdmin(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Map<String , Object> response = profileFacade.getAdminData(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping ("/add-amount")
    public ResponseEntity<?> addAmount(@RequestHeader("Authorization") String token ,
                                       @RequestBody Map<String, Object> driverData) {
        try {
            token = token.replace("Bearer ", "");
            driverData.put("jwt" , token);
            profileFacade.addAmount(driverData);
            return new ResponseEntity<>("amount added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
