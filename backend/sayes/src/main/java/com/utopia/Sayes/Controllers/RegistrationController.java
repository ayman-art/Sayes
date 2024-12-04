package com.utopia.Sayes.Controllers;

import com.utopia.Sayes.Facades.RegistrationFacade;
import com.utopia.Sayes.Modules.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    RegistrationFacade facade;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> userdata){
        System.out.println(userdata);
        try {
            String jwt = facade.loginUser((String) userdata.get("name"),
                    (String) userdata.get("password"));
            return new ResponseEntity<>((Object) Map.of(
                    "jwt", jwt
            ), HttpStatusCode.valueOf(200));

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
                jwt = facade.RegisterDriver((String) userdata.get("name"),
                    (String) userdata.get("password"), (String)userdata.get("plateNumber"),
                    Integer.parseInt((String)userdata.get("licenseNumber")));
            else if(Objects.equals(role, "lot-manager")){
                System.out.println("pass 1");
                jwt = facade.RegisterManager((String) userdata.get("name"),
                        (String) userdata.get("password"));

            }else return new ResponseEntity<>(Map.of(
                    "message", "Bad request : could not find role"
                ),HttpStatusCode.valueOf(500));
            return new ResponseEntity<>((Object) Map.of(
                    "jwt", jwt
            ), HttpStatusCode.valueOf(200));
        }catch(Exception e){
            return new ResponseEntity<>(Map.of(
                    "message", "Registration failed: "+ e.getMessage()
            ),  HttpStatusCode.valueOf(500));
        }

    }
    @GetMapping("/auth")
    public ResponseEntity<?> authUser(@RequestHeader("Authorization") String jwt){
        System.out.println("...");
        try{
            String token = jwt.replace("Bearer ", "");
            Authentication.parseToken(token);
            return new ResponseEntity<>((Object) Map.of(
                    "message", "User Authenticated successfully"
            ), HttpStatusCode.valueOf(200));
        }catch (Exception e){
            return new ResponseEntity<>((Object) Map.of(
                    "jwt", jwt
            ), HttpStatusCode.valueOf(401));
        }
    }

}
