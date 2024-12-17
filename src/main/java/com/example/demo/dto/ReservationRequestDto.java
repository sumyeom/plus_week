package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationRequestDto {
    @NotNull
    private Long itemId;
    @NotNull
    private Long userId;
    @NotNull
    private LocalDateTime startAt;
    @NotNull
    private LocalDateTime endAt;

    public ReservationRequestDto(long l, long l1, LocalDateTime startAt, LocalDateTime endAt) {
    }

    public ReservationRequestDto() {
    }
}
