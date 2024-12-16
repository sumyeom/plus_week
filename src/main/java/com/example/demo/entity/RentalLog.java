package com.example.demo.entity;

import com.example.demo.constants.RentalLogType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RentalLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logMessage;

    private RentalLogType logType; // SUCCESS, FAILURE

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public RentalLog(Reservation reservation, String logMessage, RentalLogType logType) {
        this.reservation = reservation;
        this.logMessage = logMessage;
        this.logType = logType;
    }

    public RentalLog() {}
}
