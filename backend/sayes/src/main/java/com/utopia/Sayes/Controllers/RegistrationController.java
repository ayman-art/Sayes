package com.utopia.Sayes.Controllers;

import com.utopia.Sayes.Facades.RegistrationFacade;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@CrossOrigin()
@RestController
@RequestMapping("/users")
public class RegistrationController {
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> userdata){
        System.out.println(userdata);
        try {
            String jwt = RegistrationFacade.loginUser((String) userdata.get("name"),
                    (String) userdata.get("password"));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwt);
            return new ResponseEntity<>((Object) Map.of(
                    "message", "User logged in successfully"
            ), headers, HttpStatusCode.valueOf(200));

        }catch (Exception e){
            return new ResponseEntity<>(Map.of(
                    "message", "Wrong Credentials "+ e.getMessage()
            ),  HttpStatusCode.valueOf(401));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> userdata){
        System.out.println(userdata);
        try{
            String role =(String) userdata.get("role");
            String jwt;
            if(Objects.equals(role, "driver"))
                jwt = RegistrationFacade.RegisterDriver((String) userdata.get("name"),
                    (String) userdata.get("password"), (String)userdata.get("plateNumber"),
                    Integer.parseInt((String)userdata.get("licenseNumber")));
            else if(Objects.equals(role, "lot-manager")){
                System.out.println("pass 1");
                jwt = RegistrationFacade.RegisterManager((String) userdata.get("name"),
                        (String) userdata.get("password"));

            }else return new ResponseEntity<>(Map.of(
                    "message", "Bad request : could not find role"
                ),HttpStatusCode.valueOf(500));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwt);
            return new ResponseEntity<>((Object) Map.of(
                    "message", "User registered successfully"
            ), headers, HttpStatusCode.valueOf(200));
        }catch(Exception e){
            return new ResponseEntity<>(Map.of(
                    "message", "Registration failed: "+ e.getMessage()
            ),  HttpStatusCode.valueOf(500));
        }

    }

}
