package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.constants.RentalLogType;
import com.example.demo.constants.ReservationStatus;
import com.example.demo.constants.Role;
import com.example.demo.dto.*;
import com.example.demo.filter.RoleFilter;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    Authentication TEST_USER = new Authentication(1L, Role.USER);

    @Test
    @DisplayName("예약 생성 성공")
    void createReservation_shouldReturn200() throws Exception {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);

        ReservationRequestDto requestDto = new ReservationRequestDto(itemId, userId, startDate, endDate);

        ReservationResponseDto responseDto = new ReservationResponseDto(
                1L,"name","itemName", ReservationStatus.PENDING,startDate,endDate
        );

        ReservationRentalLogResponseDto reservationRentalLogResponseDto =
                new ReservationRentalLogResponseDto(responseDto, 1L,"logMessage",RentalLogType.CREATE);

        given(reservationService.createReservation(requestDto.getItemId(),requestDto.getUserId(),requestDto.getStartAt(),requestDto.getEndAt())).willReturn(reservationRentalLogResponseDto);

        // when & then
        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .sessionAttr(GlobalConstants.USER_AUTH, TEST_USER))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservationResponseDto.id").value(reservationRentalLogResponseDto.getReservationResponseDto().getId()))
                .andExpect(jsonPath("$.reservationResponseDto.nickname").value(reservationRentalLogResponseDto.getReservationResponseDto().getNickname()))
                .andExpect(jsonPath("$.rentalId").value(reservationRentalLogResponseDto.getRentalId()));

    }

    @Test
    @DisplayName("createDto null 존재")
    void createReservation_withInvalidInput_badRequest() throws Exception {
        // given
        ReservationRequestDto requestDto = new ReservationRequestDto();

        // when & then
        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .sessionAttr(GlobalConstants.USER_AUTH, TEST_USER))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("업데이트 성공")
    void updateReservation_success() throws Exception {

        // given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        UpdateReservationRequestDto requestDto = new UpdateReservationRequestDto("APPROVED");
        ReservationResponseDto responseDto = new ReservationResponseDto(
                1L,"name","itemName",ReservationStatus.PENDING,startDate,endDate
        );

        when(reservationService.updateReservationStatus(1L,requestDto.getStatus()))
                .thenReturn(responseDto);

        //when
        mockMvc.perform(patch("/reservations/{id}/update-status",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
                .sessionAttr(GlobalConstants.USER_AUTH, TEST_USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()));

    }

    @Test
    @DisplayName("updateDto null 존재")
    void updateReservation_withInvalidInput_badRequest() throws Exception {

        // given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        UpdateReservationRequestDto requestDto = new UpdateReservationRequestDto(null);
        ReservationResponseDto responseDto = new ReservationResponseDto(
                1L,"name","itemName",ReservationStatus.PENDING,startDate,endDate
        );

        when(reservationService.updateReservationStatus(1L,requestDto.getStatus()))
                .thenReturn(responseDto);

        //when
        mockMvc.perform(patch("/reservations/{id}/update-status",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                        .sessionAttr(GlobalConstants.USER_AUTH, TEST_USER))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("모든 예약 조회")
    void findAll_allReservations() throws Exception{
        // given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);

        List<ReservationResponseDto> dtos = List.of(
                new ReservationResponseDto(
                        1L,"name","itemName",ReservationStatus.PENDING,startDate,endDate),
                new ReservationResponseDto(
                        2L,"name2","itemName2",ReservationStatus.PENDING,startDate,endDate
                ));
        when(reservationService.getReservations()).thenReturn(dtos);

        // when & then
        mockMvc.perform(get("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr(GlobalConstants.USER_AUTH,TEST_USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dtos.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(dtos.get(1).getId()));
    }

    @Test
    @DisplayName("아이템과 유저로 예약 조회")
    void searchAllTest() throws Exception {
        //given
        Long userId = 1L;
        Long itemId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        MockHttpSession mockHttpSession = new MockHttpSession();
        List<ReservationResponseDto> dtos = List.of(
                new ReservationResponseDto(
                        1L,"name","itemName",ReservationStatus.PENDING,startDate,endDate),
                new ReservationResponseDto(
                        2L,"name2","itemName2",ReservationStatus.PENDING,startDate,endDate
                ));
        when(reservationService.searchAndConvertReservations(userId,itemId))
                .thenReturn(dtos);
        //then
        mockMvc.perform(get("/reservations/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(GlobalConstants.USER_AUTH,TEST_USER)
                        .param("userId",String.valueOf(userId))
                        .param("itemId",String.valueOf(itemId)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[1].id").value(2L));
    }
}