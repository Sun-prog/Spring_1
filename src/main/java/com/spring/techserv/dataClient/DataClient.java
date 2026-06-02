package com.spring.techserv.dataClient;

import com.spring.techserv.dto.BookingRequestDTO;
import com.spring.techserv.dto.BookingResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DataClient {
    List<Integer> processBooking(List<BookingResponseDTO> bookingRequest);
}
