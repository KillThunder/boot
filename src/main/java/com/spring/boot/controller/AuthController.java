package com.spring.boot.controller;

import com.spring.boot.pojo.User;
import com.spring.boot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

//    @PostMapping(value = "/auth/login")
    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestParam("username") String username, @RequestParam("password") String password) throws AuthenticationException {
        //  @RequestBody JwtAuthenticationRequest authenticationRequest
        String token = authService.login(username, password);

        // Return the token
//        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        return ResponseEntity.ok(token);
    }

    @GetMapping(value = "/auth/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
//            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
            return ResponseEntity.ok(refreshedToken);
        }
    }

    @PostMapping(value = "${jwt.route.authentication.register}")
    public User register(@RequestBody User addedUser) throws AuthenticationException {
        return authService.register(addedUser);
    }
}
