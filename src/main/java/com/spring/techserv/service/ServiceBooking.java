package com.spring.techserv.service;

import com.spring.techserv.constants.BookingStatus;
import com.spring.techserv.constants.Role;
import com.spring.techserv.dto.AdvNotification;
import com.spring.techserv.dto.BookingCostResponseDTO;
import com.spring.techserv.dto.BookingRequestDTO;
import com.spring.techserv.dto.BookingResponseDTO;
import com.spring.techserv.entity.Booking;
import com.spring.techserv.entity.TechService;
import com.spring.techserv.entity.User;
import com.spring.techserv.exception.BookingException;
import com.spring.techserv.mapper.BookingMapper;
import com.spring.techserv.repository.BookingRepository;
import com.spring.techserv.repository.ServiceRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
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
    private final UserService userService;
    private final NotificationService notificationService;

    public Long registerBooking(@Valid BookingRequestDTO bookingRequest) {

        TechService techService = serviceRepository.findById(bookingRequest.idService())
                .orElseThrow(() -> new BookingException(HttpStatus.BAD_REQUEST, "Указанная услуга не существует"));
        User user = userService.getByUsername(bookingRequest.username());
        Booking booking = bookingMapper.mapToEntity(bookingRequest);
        booking.setUser(user);
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
        System.out.println("тех сервис нашли");
        Booking bookingDB = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new BookingException(HttpStatus.BAD_REQUEST, "Указанная бронь не существует"));
        System.out.println("заказ нашли");

        User user = userService.getByUsername(bookingRequest.username());
        System.out.println("пользователя нашли");
        Booking bookingUpdate = bookingMapper.mapToEntity(bookingRequest);
        bookingDB.setTechService(techService);
        bookingDB.setUser(user);
        bookingDB.setFixedCost(techService.getCost());

        if (Objects.nonNull(bookingUpdate.getTime())) {
            bookingDB.setTime(bookingUpdate.getTime());
            System.out.println("время изменили");
        }
        String messageText="Здравствуйте,"+ user.getUsername()+
                ".\n  Данные заказа изменены. \n Актуальные данные о бронировании:.\n  Тип услуги: "+
                bookingDB.getTechService().getTitle() +"\n Стоимость: "+bookingDB.getFixedCost()
                +" руб. \n Время: "+bookingDB.getTime();
        bookingRepository.save(bookingDB);
        AdvNotification advNotification = new AdvNotification(messageText,user);
        notificationService.sendNotification(advNotification, user);
        return bookingDB.getIdBooking();
    }

    public Long setSaleBooking(@Positive long idBooking, @Positive @DecimalMax(value = "100", inclusive = false, message = "Скидка должна быть  меньше 100%") BigDecimal discountPercentage) {
        Booking bookingDB = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new BookingException(HttpStatus.BAD_REQUEST, "Указанная бронь не существует"));
        System.out.println("заказ нашли");
        BigDecimal newCost = BigDecimal.valueOf(100)
                .subtract(discountPercentage)
                .multiply(bookingDB.getFixedCost())
                .divide(BigDecimal.valueOf(100));
        bookingDB.setFixedCost(newCost);
        bookingRepository.save(bookingDB);
        return bookingDB.getIdBooking();

    }

    public Long cancelBooking(@Positive long idBooking) {
        Booking bookingDB = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new BookingException(HttpStatus.BAD_REQUEST, "Указанная бронь не существует"));
        System.out.println("заказ нашли, отмена выполняется пользователем с ролью "+userService.getCurrentUser().getRole());
        bookingDB.setBookingStatus(BookingStatus.CANCELLED);
        User user = bookingDB.getUser();
        if(userService.getCurrentUser().getRole()!= Role.ROLE_USER){
            String messageText="Здравствуйте,"+ user.getUsername()+
                    ".\n  Ваш заказ отменен. \n Приносим извинения и дарим скидку на следующее бронирование в размере 1%";
            bookingRepository.save(bookingDB);
            notificationService.sendNotification(new AdvNotification(messageText,user), user);
        }

        bookingRepository.save(bookingDB);
        return bookingDB.getIdBooking();
    }
}
