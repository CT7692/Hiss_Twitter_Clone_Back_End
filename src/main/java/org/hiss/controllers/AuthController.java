package org.hiss.controllers;

import org.hiss.DTO.LoginDTO;
import org.hiss.DTO.PasswordChangeDTO;
import org.hiss.DTO.RegisterDTO;
import org.hiss.response.LoginResponse;
import org.hiss.response.PasswordChangeResponse;
import org.hiss.response.RequestResponse;
import org.hiss.services.AuthService;
import org.hiss.services.JWTService;
import org.hiss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RequestResponse> register(
            @RequestBody RegisterDTO registerDTO) {
        RequestResponse requestResponse = authService.register(registerDTO);
        boolean success = requestResponse.isRequest_processed();
        ResponseEntity<RequestResponse> result = null;

        if(success)
            result = new ResponseEntity<>(requestResponse, HttpStatus.CREATED);
        else
            result = new ResponseEntity<>(requestResponse, HttpStatus.BAD_REQUEST);

        return result;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Konnichiha, sekai.");
    }

    @PostMapping("/update_password")
    public ResponseEntity<PasswordChangeResponse> updatePassword(
            Principal principal,
            @RequestBody PasswordChangeDTO passwordChangeDTO) {

        boolean success = authService.changePassword(
                principal.getName(),
                passwordChangeDTO);

        if(success)
            return ResponseEntity.ok(new PasswordChangeResponse(true));
        else
            return new ResponseEntity<>(new PasswordChangeResponse(false), HttpStatus.BAD_REQUEST);
    }


}
