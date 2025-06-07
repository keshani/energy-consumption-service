package com.enefit.energyconsumption.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enefit.energyconsumption.config.model.AuthenticationRequest;
import com.enefit.energyconsumption.config.model.AuthenticationResponse;
import com.enefit.energyconsumption.config.util.JWTUtil;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class AuthenticationController {

   private final AuthenticationManager authenticationManager;
   private final AppUserDetailsService userDetailsService;
   private final JWTUtil jWTUtil;

    public AuthenticationController(final AuthenticationManager authenticationManager,
            final AppUserDetailsService userDetailsService, final JWTUtil jWTUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jWTUtil = jWTUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody
    AuthenticationRequest authenticationRequest ) {
        String value = new BCryptPasswordEncoder().encode("admin456");
        log.info("Creating authentication token for user {}",value);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword() )
            );
        } catch (Exception ex) {
            log.error("Invalid username or password for user {} ",  authenticationRequest.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }
        final UserDetails userDetil = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwtToken = jWTUtil.generateToken(userDetil.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
}
