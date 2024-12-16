package com.example.demo.constants;

import com.example.demo.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    CANCELED("CANCELED"),
    EXPIRED("EXPIRED");

    private final String status;

    public static ReservationStatus of(String reservationStatus) {
        for (ReservationStatus status : values()) {
            if (status.getStatus().equals(reservationStatus)) {
                return status;
            }
        }

        throw new IllegalArgumentException("유효하지 않은 상태 값입니다.: " + reservationStatus);
    }

}
