package com.utopia.Sayes.Controllers;

import com.utopia.Sayes.Facades.ReportingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin()
@RestController
@RequestMapping("/report")
public class ReportingController {
    @Autowired
    ReportingFacade reportingFacade;
    @GetMapping("/get-logs")
    public ResponseEntity<?> getLogs(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Map<String , Object> response = reportingFacade.getLogs(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-manager-lot-report")
    public ResponseEntity<?> getLotsReportForManager(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Map<String , Object> response = reportingFacade.getLotsReportForManager(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-top-users")
    public ResponseEntity<?> getTopUsers(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Map<String , Object> response = reportingFacade.getTopUsers(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-top-lots")
    public ResponseEntity<?> getTopLots(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Map<String , Object> response = reportingFacade.getTopLots(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/generate-top-users-report")
    public ResponseEntity<byte[]> generateTopUsersReport(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            byte[] report = reportingFacade.getTopUsersReport(token);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=top_users_report.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            return new ResponseEntity<>(report, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/generate-top-lots-report")
    public ResponseEntity<byte[]> generateTopLotsReport(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            byte[] report = reportingFacade.getTopLotsReport(token);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=top_lots_report.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            return new ResponseEntity<>(report, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/generate-violations-report")
    public ResponseEntity<byte[]> generateViolationsReport(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            byte[] report = reportingFacade.getViolationsReport(token);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=top_lots_report.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            return new ResponseEntity<>(report, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.BAD_REQUEST);
        }
    }
}
