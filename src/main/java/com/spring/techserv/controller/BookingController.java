package com.spring.techserv.controller;

import com.spring.techserv.constants.BookingStatus;
import com.spring.techserv.dto.BookingCostResponseDTO;
import com.spring.techserv.dto.BookingRequestDTO;
import com.spring.techserv.dto.BookingResponseDTO;
import com.spring.techserv.dto.ServiceRequestDTO;
import com.spring.techserv.service.ServiceBooking;
import com.spring.techserv.service.ServiceTechServ;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/booking")
@RestController
public class BookingController {
    private final ServiceBooking serviceBooking;

    //Создание брони
    //{"idService": 1, "time" : "2027-01-19T15:10:06.780         "}
    @PostMapping
    public ResponseEntity<?>  createBooking(@Valid @RequestBody BookingRequestDTO bookingRequest){
        URI uri = URI.create("/api/v1/booking?id=" +
                serviceBooking.registerBooking(bookingRequest));
        return ResponseEntity.created(uri) // HttpStatu2s 201
                .build();

    }

    //Получение списка броней
    @GetMapping
    public List<BookingResponseDTO> getAllBooking(){
        return null;
    }

    //Отмена брони
    @PutMapping("/cancel/{id}")
    public Long editService(
            @Positive
            @PathVariable
                    ("id") int idBooking){
        return 0L;
    }

    //Редактирование времени брони
   /* @Future
    @RequestBody
    LocalDateTime newTime*/
    @PutMapping("/editTime/{id}")
    public int editTime(
            @Positive
            @PathVariable
                    ("id") int idBooking,

            @RequestParam
            LocalDateTime time){
        return 5;
    }

    //Получение списка завершенных броней (предотавленные услуги)
    @GetMapping("/complited")
    public List<BookingResponseDTO> getComplitedBookings() {
        //log.info("GET request with anim
        // al type = {}", type);
        return null;
    }

    //Получение брони по дате времени
    @GetMapping("/byTime")
    public BookingResponseDTO getByTime(
            @RequestParam("time")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDateTime time) {
            return serviceBooking.findByTime(time);
    }

    // Получение выручки за период
    @GetMapping(path = "/costByPeriod", produces = "application/json")
    public ResponseEntity<HashMap<LocalDate, BigDecimal>> getBookingByPeriod(
            @RequestParam("timeStart")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime timeStart,
            @RequestParam("timeStop")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime timeStop ){
        //log.info("GET request with animal type = {}", type);
            return new ResponseEntity<>(serviceBooking.findCostByPeriod(timeStart,timeStop), HttpStatus.OK);
    }

    //Редактирование брони любого пользователя.
    @PostMapping("/{id}")
    public Long editBooking(
            @Positive
            @PathVariable
                    ("id") long idBooking,
            @Valid @RequestBody BookingRequestDTO bookingRequest){
          serviceBooking.updateBooking(
                 idBooking, bookingRequest);
        return idBooking;
    }

}



