package com.spring.techserv.entity;

import com.spring.techserv.constants.BookingStatus;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Builder
@Getter
@Setter
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBooking", nullable = false, unique = true)
    private long idBooking;

    @ManyToOne
    @JoinColumn(name = "idService",nullable = false)
    private TechService techService;

    @ManyToOne
    @JoinColumn(name = "nameUser",nullable = false)
    private User user;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "fixedCost")
    private BigDecimal fixedCost;

    @Timestamp
    @Column(name = "timeCreate")
    private LocalDateTime timeCreate;

    @Value("ACTIVE")
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

}
