package com.spring.techserv.service;

import com.spring.techserv.dto.BookingCostResponseDTO;
import com.spring.techserv.dto.BookingRequestDTO;
import com.spring.techserv.dto.BookingResponseDTO;
import com.spring.techserv.entity.Booking;
import com.spring.techserv.entity.TechService;
import com.spring.techserv.exception.BookingException;
import com.spring.techserv.mapper.BookingMapper;
import com.spring.techserv.repository.BookingRepository;
import com.spring.techserv.repository.ServiceRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ServiceBooking {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ServiceRepository serviceRepository;

    public Long registerBooking(@Valid BookingRequestDTO bookingRequest) {

        TechService techService = serviceRepository.findById(bookingRequest.idService())
                .orElseThrow(() -> new BookingException(HttpStatus.BAD_REQUEST, "Указанная услуга не существует"));

        Booking booking = bookingMapper.mapToEntity(bookingRequest);
        booking.setTechService(techService);
        booking.setFixedCost(techService.getCost());
        bookingRepository.save(booking);
        return booking.getIdBooking();
    }

    public BookingResponseDTO findByTime(LocalDateTime time) {
        Booking booking = bookingRepository.findByTime(time)
                .orElseThrow(() -> new BookingException(HttpStatus.BAD_REQUEST, "брони с указанной датой нет"));
        bookingMapper.entityToMap(booking);
        return bookingMapper.entityToMap(booking);
    }

    public HashMap<LocalDate, BigDecimal> findCostByPeriod(LocalDateTime timeStart,LocalDateTime timeEnd) {
        System.out.println("ЗАПРОС СПИСКА");
        List<Booking> bookings  = bookingRepository.findByFilter(timeStart, timeEnd);
        if (bookings.isEmpty()) throw new BookingException(HttpStatus.NOT_FOUND, "Записи не найдены");

        HashMap<LocalDate, BigDecimal> proceeds = new HashMap<>();
        LocalDate date;
        for (Booking booking:bookings){
            date=booking.getTime().toLocalDate();
            if(proceeds.containsKey(date)){proceeds.put(date,proceeds.get(date).add(booking.getFixedCost()));}
            else{proceeds.put(date,booking.getFixedCost());}
        }

        return proceeds;
    }


    public Long updateBooking(@Positive Long idBooking, @Valid BookingRequestDTO bookingRequest) {
        TechService techService = serviceRepository.findById(bookingRequest.idService())
                .orElseThrow(() -> new BookingException(HttpStatus.BAD_REQUEST, "Указанная услуга не существует"));
        Booking bookingDB = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new BookingException(HttpStatus.BAD_REQUEST, "Указанная бронь не существует"));
        Booking bookingUpdate = bookingMapper.mapToEntity(bookingRequest);
        bookingDB.setTechService(techService);

        if (Objects.nonNull(bookingUpdate.getTime())) {
            bookingDB.setTime(bookingUpdate.getTime());
        }
        bookingRepository.save(bookingDB);
        return bookingDB.getIdBooking();
    }
}
