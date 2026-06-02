package com.spring.techserv.dataClient;

import com.spring.techserv.dto.BookingRequestDTO;
import com.spring.techserv.dto.BookingResponseDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Profile("rest")
public class RestClientDataClient implements DataClient {

    private final RestClient restClient;

    // Настраиваем RestClient через конструктор (URL берем из настроек)
    public RestClientDataClient(RestClient.Builder builder, org.springframework.core.env.Environment env) {
        String baseUrl = env.getProperty("api.base-url");
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public List<Integer> processBooking(List<BookingResponseDTO> bookingRequest) {
        return restClient.post()
                .uri("api/bookings")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookingRequest)
                .retrieve()
                .body(List.class);
    }


    public String getData(Long id) {
        return restClient.get()
                .uri("api/num/{id}", id)
                .retrieve()
                .body(String.class);
    }
    public String getData(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}