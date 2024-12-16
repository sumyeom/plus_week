package com.example.demo.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalLogType {
    CREATE("CREATE"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    private final String type;

    public static RentalLogType of(String rentalLogType) {
        for (RentalLogType type : values()) {
            if (type.getType().equals(rentalLogType)) {
                return type;
            }
        }

        throw new IllegalArgumentException("유효하지 않은 타입 값입니다.: " + rentalLogType);
    }

}
