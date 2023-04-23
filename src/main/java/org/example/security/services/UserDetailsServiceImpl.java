package org.example.security.services;

import lombok.AllArgsConstructor;
import org.example.dtos.*;
import org.example.security.entities.User;
import org.example.security.jwt.JwtUtils;
import org.example.security.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with phone: " + phone));

        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));

        return UserDetailsImpl.builder()
                .id(user.getId())
                .name(user.getName())
                .authorities(authorities)
                .password(user.getPassword())
                .phone(user.getPhone())
                .build();
    }

    public ResponseEntity<MessageResponse> register(RegisterRequest registerRequest) {
        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Phone is already taken!"));
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<MessageResponse>(new MessageResponse("User registered successfully!"),
                HttpStatus.CREATED);
    }

    public ResponseEntity<TokenResponse> login(AuthenticationManager authenticationManager,
                                                 LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhone(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new ResponseEntity<TokenResponse>(new TokenResponse(jwt), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> getName(Principal principal) {
        var user = userRepository.findByPhone(principal.getName());
        return new ResponseEntity<MessageResponse>(new MessageResponse("your name is " + user.get().getName()), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> updateName(Principal principal, UpdateNameRequest updateNameRequest) {
        var user = userRepository.findByPhone(principal.getName()).get();
        var oldName = user.getName();
        user.setName(updateNameRequest.getName());
        userRepository.save(user);
        return new ResponseEntity<MessageResponse>(new MessageResponse("the name already updated from " + oldName + " to be " + updateNameRequest.getName()), HttpStatus.OK);
    }
}
