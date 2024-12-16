package com.example.demo.constants;

import com.example.demo.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    NORMAL("NORMAL"),
    BLOCKED("BLOCKED");

    private final String status;

    public static UserStatus of(String userStatus) {
        for (UserStatus status : values()) {
            if (status.getStatus().equals(userStatus)) {
                return status;
            }
        }

        throw new IllegalArgumentException("유효하지 않은 상태 값입니다.: " + userStatus);
    }
}
