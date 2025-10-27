package com.salesSavvy.controller;

import com.salesSavvy.dto.AuthResponse;
import com.salesSavvy.entity.UserLoginData;
import com.salesSavvy.entity.Users;
import com.salesSavvy.security.JwtUtil;
import com.salesSavvy.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsersService usersService;

    public AuthController(AuthenticationManager authenticationManager,
                         UserDetailsService userDetailsService,
                         JwtUtil jwtUtil,
                         UsersService usersService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody Users user) {
        usersService.signUp(user);
        
        AuthResponse response = new AuthResponse();
        response.setMessage("User registered successfully");
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody UserLoginData loginData) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginData.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        Users user = usersService.getUser(loginData.getUsername());

        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setMessage("Login successful");

        return ResponseEntity.ok(response);
    }

    // Test endpoint to verify JWT is working
    @GetMapping("/test")
    public ResponseEntity<String> testJwt() {
        return ResponseEntity.ok("JWT is working!");
    }
}