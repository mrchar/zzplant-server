package net.mrchar.zzplant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    @RequestMapping("/login/success")
    public ResponseEntity<Void> loginSuccess() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/login/failure")
    public ResponseEntity<Void> loginFailure() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
