package com.example.demo.service;

import com.example.demo.constants.RentalLogType;
import com.example.demo.constants.ReservationStatus;
import com.example.demo.entity.Item;
import com.example.demo.entity.RentalLog;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Users;
import com.example.demo.repository.RentalLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalLogServiceTest {

    @Mock
    private RentalLogRepository rentalLogRepository;

    @InjectMocks
    private RentalLogService rentalLogService;

    @Test
    void save_success() {
        //given
        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation = new Reservation(item,owner, ReservationStatus.CANCELED,startAt,endAt);

        RentalLog rentalLog = new RentalLog(reservation,"logmessage", RentalLogType.CREATE);
        when(rentalLogRepository.save(rentalLog)).thenReturn(rentalLog);
        //when
        RentalLog saved = rentalLogService.save(rentalLog);

        //then
        assertNotNull(saved);
        assertEquals(rentalLog.getLogType(),saved.getLogType());

    }
}