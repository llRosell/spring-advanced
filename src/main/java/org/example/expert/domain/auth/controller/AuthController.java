package org.example.expert.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.AuthSigninRequest;
import org.example.expert.domain.auth.dto.request.AuthSignupRequest;
import org.example.expert.domain.auth.dto.response.AuthSigninResponse;
import org.example.expert.domain.auth.dto.response.AuthSignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public AuthSignupResponse signup(@Valid @RequestBody AuthSignupRequest authSignupRequest) {
        return authService.signup(authSignupRequest);
    }

    @PostMapping("/auth/signin")
    public AuthSigninResponse signin(@Valid @RequestBody AuthSigninRequest authSigninRequest) {
        return authService.signin(authSigninRequest);
    }
}
