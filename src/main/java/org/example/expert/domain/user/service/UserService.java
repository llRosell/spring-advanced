package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserPasswordChangeRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse findUserById(long userId) {
        User user = findUserByIdOrThrow(userId);
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserPasswordChangeRequest userPasswordChangeRequest) {
        validateNewPassword(userPasswordChangeRequest.getNewPassword());

        User user = findUserByIdOrThrow(userId);

        validateOldPassword(userPasswordChangeRequest.getOldPassword(), user.getPassword());
        validateNewPasswordAgainstOld(userPasswordChangeRequest.getNewPassword(), user.getPassword());

        user.changePassword(passwordEncoder.encode(userPasswordChangeRequest.getNewPassword()));
    }

    // 유틸리티 메서드들
    private User findUserByIdOrThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));
    }

    private void validateOldPassword(String oldPassword, String currentPassword) {
        if (!passwordEncoder.matches(oldPassword, currentPassword)) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }
    }

    private void validateNewPassword(String newPassword) {
        if (newPassword.length() < 8 ||
                !newPassword.matches(".*\\d.*") ||
                !newPassword.matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }

    private void validateNewPasswordAgainstOld(String newPassword, String currentPassword) {
        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }
    }
}
