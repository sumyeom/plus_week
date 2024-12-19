package com.example.demo.repository;

import com.example.demo.config.QuerydslConfig;
import com.example.demo.constants.ReservationStatus;
import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByIdOrElseThrow_success() {
        // given
        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");
        userRepository.saveAndFlush(owner);
        userRepository.saveAndFlush(manager);

        Item item = new Item("reservationtestitem", "reservationtestitem", manager, owner);
        Item savedItem = itemRepository.saveAndFlush(item);

        Reservation reservation = new Reservation(savedItem,owner, ReservationStatus.PENDING,startAt,endAt);
        Reservation savedReservation = reservationRepository.saveAndFlush(reservation);

        // when
        Reservation findReservation = reservationRepository.findByIdOrElseThrow(savedReservation.getId());

        // then
        assertNotNull(findReservation);
        assertEquals(findReservation.getId(),savedReservation.getId());

    }

    @Test
    void findByIdOrElseThrow_failure() {
        // given
        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");
        userRepository.saveAndFlush(owner);
        userRepository.saveAndFlush(manager);

        Item item = new Item("reservationtestitem", "reservationtestitem", manager, owner);
        Item savedItem = itemRepository.saveAndFlush(item);

        Reservation reservation = new Reservation(savedItem,owner, ReservationStatus.PENDING,startAt,endAt);
        Reservation savedReservation = reservationRepository.saveAndFlush(reservation);

        // when
        InvalidDataAccessApiUsageException exception =  assertThrows(InvalidDataAccessApiUsageException.class, ()->reservationRepository.findByIdOrElseThrow(100L));

        // then
        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", exception.getMessage());

    }
}