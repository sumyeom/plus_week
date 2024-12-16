package com.example.demo.dto;

import com.example.demo.constants.RentalLogType;
import lombok.Getter;

@Getter
public class ReservationRentalLogResponseDto {
    private ReservationResponseDto reservationResponseDto;
    private Long rentalId;
    private String logMessage;
    private RentalLogType logType;

    public ReservationRentalLogResponseDto(ReservationResponseDto reservationResponseDto, Long rentalId, String logMessage, RentalLogType logType) {
        this.reservationResponseDto = reservationResponseDto;
        this.rentalId = rentalId;
        this.logMessage = logMessage;
        this.logType = logType;
    }
}

