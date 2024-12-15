package com.example.demo.repository;

import com.example.demo.entity.Reservation;

import java.util.List;

public interface ReservationQuerydslRepository {
    List<Reservation> searchReservationQuerydsl(Long userId, Long itemId);
}
