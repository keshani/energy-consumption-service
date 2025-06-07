package com.enefit.energyconsumption.config.model;

/**
 * Model representing a user login respond with JWTToken.
 * @author Keshani
 * @since 2025/05/31
 */
public class AuthenticationResponse {

    private final String jwtToken;

    public AuthenticationResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
