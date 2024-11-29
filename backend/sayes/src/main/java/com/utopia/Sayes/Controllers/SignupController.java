package com.utopia.Sayes.Controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin()
@RestController
@RequestMapping("/users")
public class SignupController {
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> userdata){
        System.out.println(userdata);
        return new ResponseEntity<>(userdata, HttpStatusCode.valueOf(200));
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> userdata){
        System.out.println(userdata);
        return new ResponseEntity<>(userdata, HttpStatusCode.valueOf(200));
    }

}
