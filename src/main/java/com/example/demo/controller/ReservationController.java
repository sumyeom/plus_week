package com.example.demo.controller;

import com.example.demo.dto.ReservationRentalLogResponseDto;
import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.dto.UpdateReservationRequestDto;
import com.example.demo.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationRentalLogResponseDto> createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationRentalLogResponseDto dto = reservationService.createReservation(reservationRequestDto.getItemId(),
                                            reservationRequestDto.getUserId(),
                                            reservationRequestDto.getStartAt(),
                                            reservationRequestDto.getEndAt());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/update-status")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Long id, @RequestBody UpdateReservationRequestDto dto) {
        ReservationResponseDto responseDto = reservationService.updateReservationStatus(id, dto.getStatus());

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        List<ReservationResponseDto> reservationResponseDtos = reservationService.getReservations();
        return new ResponseEntity<>(reservationResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponseDto>> searchAll(@RequestParam(required = false) Long userId,
                          @RequestParam(required = false) Long itemId) {
        List<ReservationResponseDto> reservationResponseDtos = reservationService.searchAndConvertReservations(userId, itemId);
        return new ResponseEntity<>(reservationResponseDtos, HttpStatus.OK);
    }
}
