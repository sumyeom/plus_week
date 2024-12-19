package com.example.demo.service;

import com.example.demo.constants.RentalLogType;
import com.example.demo.constants.ReservationStatus;
import com.example.demo.dto.ReservationRentalLogResponseDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.RentalLog;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Users;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalLogService rentalLogService;
    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("이미 예약되어있을 때")
    void createReservation_haveReservations() {
        // given
        Long itemId = 1L;
        Long userId = 1L;
        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);

        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");

        Item item = new Item("test", "description", manager, owner);

        List<Reservation> reservations = List.of(
                new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt),
                new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt)
        );

        //List<Reservation> reservations = List.of();
        when(reservationRepository.findConflictingReservations(itemId,startAt,endAt)).thenReturn(reservations);

        ReservationConflictException exception = assertThrows(ReservationConflictException.class, ()->
                reservationService.createReservation(itemId, userId, startAt, endAt));

        //then
        assertEquals("해당 물건은 이미 그 시간에 예약이 있습니다.", exception.getMessage());

    }

    @Test
    @DisplayName("예약 성공")
    void createReservation_success() {
        // given
        Long itemId = 1L;
        Long userId = 1L;
        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);

        Reservation reservation = new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt);
        RentalLog rentalLog = new RentalLog(reservation, "로그 메시지",RentalLogType.CREATE);

        List<Reservation> reservations = List.of();
        ReservationResponseDto responseDto = new ReservationResponseDto(
          1L,"owner","itemName",ReservationStatus.PENDING,startAt,endAt
        );
        ReservationRentalLogResponseDto reservationRentalLogResponseDto = new ReservationRentalLogResponseDto(
                responseDto,1L,"logMessage", RentalLogType.CREATE
        );

        when(reservationRepository.findConflictingReservations(itemId,startAt,endAt)).thenReturn(reservations);
        when(itemRepository.findByIdOrElseThrow(itemId)).thenReturn(item);
        when(userRepository.findByIdOrElseThrow(userId)).thenReturn(owner);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(rentalLogService.save(any(RentalLog.class))).thenReturn(rentalLog);

        // when
        ReservationRentalLogResponseDto resultDto =
                reservationService.createReservation(itemId, userId, startAt, endAt);

        // then
        assertNotNull(resultDto);
        assertEquals("owner",resultDto.getReservationResponseDto().getNickname());
        assertEquals("itemName",resultDto.getReservationResponseDto().getItemName());
        assertEquals(RentalLogType.CREATE,resultDto.getLogType());
        verify(reservationRepository, times(1))
                .findConflictingReservations(itemId,startAt,endAt);


    }

    @Test
    @DisplayName("모든 예약 목록 조회")
    void getReservations_allReservations() {
        // given
        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);

        Reservation reservation1 = new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt);
        Reservation reservation2 = new Reservation(item,manager, ReservationStatus.PENDING,startAt,endAt);
        when(reservationRepository.findAllUserItem()).thenReturn(List.of(reservation1,reservation2));

        // when
        List<ReservationResponseDto> responseDtos = reservationService.getReservations();

        // then
        assertNotNull(responseDtos);
        assertEquals(2,responseDtos.size());

        ReservationResponseDto responseDto1 = responseDtos.get(0);
        assertEquals("owner",responseDto1.getNickname());
    }

    @Test
    void searchAndConvertReservations() {
        // given
        Long itemId = 1L;
        Long userId = 1L;

        List<ReservationResponseDto> resultDtos = reservationService.searchAndConvertReservations(itemId,userId);

        assertNotNull(resultDtos);
    }

    @Test
    @DisplayName("예약 조건 조회")
    void searchReservations_success() {
        // given
        Long itemId = 1L;
        Long userId = 1L;
        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation1 = new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt);
        Reservation reservation2 = new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt);

        List<Reservation> reservations = List.of(reservation1,reservation2);
        when(reservationRepository.searchReservationQuerydsl(userId, itemId)).thenReturn(reservations);

        // when
        List<Reservation> savedReservations = reservationService.searchReservations(userId, itemId);

        // then
        assertNotNull(savedReservations);
        assertEquals(2,savedReservations.size());

        Reservation reservation = savedReservations.get(0);
        assertEquals("itemName", reservation.getItem().getName());
    }


    @Test
    void updateReservationStatus_validInput_APPROVED_success() {
        // given
        Long reservationId = 1L;
        String status = ReservationStatus.APPROVED.toString();

        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation = new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt);
        when(reservationRepository.findByIdOrElseThrow(reservationId)).thenReturn(reservation);

        // when
        ReservationResponseDto responseDto = reservationService.updateReservationStatus(reservationId, status);

        // then
        assertNotNull(responseDto);
        assertEquals("owner",responseDto.getNickname());
        assertEquals(ReservationStatus.APPROVED, responseDto.getStatus());

    }

    @Test
    void updateReservationStatus_validInput_APPROVED_Failed() {
        // given
        Long reservationId = 1L;
        String status = ReservationStatus.APPROVED.toString();

        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation = new Reservation(item,owner, ReservationStatus.CANCELED,startAt,endAt);
        when(reservationRepository.findByIdOrElseThrow(reservationId)).thenReturn(reservation);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->
                reservationService.updateReservationStatus(reservationId, status));

        // then
        assertEquals("PENDING 상태만 APPROVED로 변경 가능합니다.", exception.getMessage());

    }

    @Test
    void updateReservationStatus_validInput_CANCELED_success() {
        // given
        Long reservationId = 1L;
        String status = ReservationStatus.CANCELED.toString();

        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation = new Reservation(item,owner, ReservationStatus.EXPIRED,startAt,endAt);
        when(reservationRepository.findByIdOrElseThrow(reservationId)).thenReturn(reservation);

        // when
        ReservationResponseDto responseDto = reservationService.updateReservationStatus(reservationId, status);

        // then
        assertNotNull(responseDto);
        assertEquals("owner",responseDto.getNickname());
        assertEquals(ReservationStatus.CANCELED, responseDto.getStatus());

    }

    @Test
    void updateReservationStatus_validInput_CANCELED_Failed() {
        // given
        Long reservationId = 1L;
        String status = ReservationStatus.CANCELED.toString();

        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation = new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt);
        when(reservationRepository.findByIdOrElseThrow(reservationId)).thenReturn(reservation);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->
                reservationService.updateReservationStatus(reservationId, status));

        // then
        assertEquals("EXPIRED 상태인 예약은 취소할 수 없습니다.", exception.getMessage());

    }

    @Test
    void updateReservationStatus_validInput_EXPIRED_success() {
        // given
        Long reservationId = 1L;
        String status = ReservationStatus.EXPIRED.toString();

        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation = new Reservation(item,owner, ReservationStatus.PENDING,startAt,endAt);
        when(reservationRepository.findByIdOrElseThrow(reservationId)).thenReturn(reservation);

        // when
        ReservationResponseDto responseDto = reservationService.updateReservationStatus(reservationId, status);

        // then
        assertNotNull(responseDto);
        assertEquals("owner",responseDto.getNickname());
        assertEquals(ReservationStatus.EXPIRED, responseDto.getStatus());

    }

    @Test
    void updateReservationStatus_validInput_EXPIRED_Failed() {
        // given
        Long reservationId = 1L;
        String status = ReservationStatus.EXPIRED.toString();

        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation = new Reservation(item,owner, ReservationStatus.CANCELED,startAt,endAt);
        when(reservationRepository.findByIdOrElseThrow(reservationId)).thenReturn(reservation);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->
                reservationService.updateReservationStatus(reservationId, status));

        // then
        assertEquals("PENDING 상태만 EXPIRED로 변경 가능합니다.", exception.getMessage());

    }

    @Test
    void updateReservationStatus_InvalidInput_Failed() {
        // given
        Long reservationId = 1L;
        String status = "INVALID";

        LocalDateTime startAt = LocalDateTime.now().minusDays(7);
        LocalDateTime endAt = LocalDateTime.now().plusDays(7);
        Users owner = new Users("user", "a@com", "owner", "0000");
        Users manager = new Users("user", "b@com", "manager", "0000");
        Item item = new Item("itemName", "description", manager, owner);
        Reservation reservation = new Reservation(item,owner, ReservationStatus.CANCELED,startAt,endAt);
        when(reservationRepository.findByIdOrElseThrow(reservationId)).thenReturn(reservation);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->
                reservationService.updateReservationStatus(reservationId, status));

        // then
        assertEquals("No enum constant com.example.demo.constants.ReservationStatus."+status, exception.getMessage());

    }
}