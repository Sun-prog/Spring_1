package com.spring.techserv.repository;

import com.spring.techserv.entity.Booking;
import com.spring.techserv.entity.TechService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByTime(LocalDateTime time);

    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings u " +
                    "WHERE  ( u.status = \'ACTIVE\') AND " +
                    " u.time > :ageFrom  AND " +
                    "u.time < :ageTo   ")
    List<Booking> findByFilter(LocalDateTime ageFrom,
                              LocalDateTime ageTo
                              );

    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings u " +
                    "WHERE  ( u.status = \'ACTIVE\')")
    List<Booking> findActiveBooking();
}