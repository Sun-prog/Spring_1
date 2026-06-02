package com.spring.techserv.dataClient;

import com.spring.techserv.dto.BookingRequestDTO;
import com.spring.techserv.dto.BookingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Profile("feign")
@FeignClient(name = "address-service", url = "http://localhost:5000", path = "/api")
public interface FeignDataClient extends DataClient {

    @Override
    @GetMapping("/bookings")
    List<Integer>  processBooking(@RequestBody List<BookingResponseDTO> bookingRequest);
}
