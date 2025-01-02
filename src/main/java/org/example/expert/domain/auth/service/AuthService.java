package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.AuthSigninRequest;
import org.example.expert.domain.auth.dto.request.AuthSignupRequest;
import org.example.expert.domain.auth.dto.response.AuthSigninResponse;
import org.example.expert.domain.auth.dto.response.AuthSignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthSignupResponse signup(AuthSignupRequest authSignupRequest) {

        // 이미 존재하는 이메일인지 가장 먼저 확인!! (Early Return - 불필요한 로직의 실행을 방지)
        if (userRepository.existsByEmail(authSignupRequest.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(authSignupRequest.getPassword());

        UserRole userRole = UserRole.of(authSignupRequest.getUserRole());

        User newUser = new User(
                authSignupRequest.getEmail(),
                encodedPassword,
                userRole
        );
        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new AuthSignupResponse(bearerToken);
    }

    public AuthSigninResponse signin(AuthSigninRequest authSigninRequest) {
        User user = userRepository.findByEmail(authSigninRequest.getEmail()).orElseThrow(
                () -> new InvalidRequestException("가입되지 않은 유저입니다."));

        if (!passwordEncoder.matches(authSigninRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new AuthSigninResponse(bearerToken);
    }
}
