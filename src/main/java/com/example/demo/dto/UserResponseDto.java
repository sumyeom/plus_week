package com.example.demo.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String role;

    public UserResponseDto(Long id, String email, String nickname, String role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
