package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.dtos.*;
import org.example.security.services.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return userDetailsServiceImpl.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return userDetailsServiceImpl.login(authenticationManager, loginRequest);
    }

    @GetMapping("/name")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MessageResponse> getName(Principal principal) {
        return userDetailsServiceImpl.getName(principal);
    }

    @PutMapping("/name")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MessageResponse> updateName(@Valid @RequestBody UpdateNameRequest updateNameRequest, Principal principal) {
        return userDetailsServiceImpl.updateName(principal, updateNameRequest);
    }

}
