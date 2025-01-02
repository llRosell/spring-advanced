package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class AuthSignupResponse {

    private final String bearerToken;

    public AuthSignupResponse(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
