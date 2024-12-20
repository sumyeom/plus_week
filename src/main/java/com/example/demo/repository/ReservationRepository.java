package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.Users;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> ,ReservationQuerydslRepository {

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.item.id = :id " +
            "AND NOT (r.endAt <= :startAt OR r.startAt >= :endAt) " +
            "AND r.status = 'APPROVED'")
    List<Reservation> findConflictingReservations(
            @Param("id") Long id,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );

    @Query("select r from Reservation r join fetch r.user join fetch r.item")
    List<Reservation> findAllUserItem();

    default Reservation findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(()->
                        new InvalidDataAccessApiUsageException("해당 ID에 맞는 값이 존재하지 않습니다."));
    }
}
