package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class AuthSigninResponse {

    private final String bearerToken;

    public AuthSigninResponse(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
