package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateReservationRequestDto {
    @NotNull
    private String status;

    public UpdateReservationRequestDto(String status) {
        this.status = status;
    }

    public UpdateReservationRequestDto() {}

}
