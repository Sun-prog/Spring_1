package com.spring.techserv.controller;

import com.spring.techserv.dto.ServiceRequestDTO;
import com.spring.techserv.dto.ServiceResponseDTO;
import com.spring.techserv.service.ServiceTechServ;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1/techserv")
@RestController
public class ServController {
    private final ServiceTechServ serviceTechServ;

    //Создание услуги
    @PostMapping
    public ResponseEntity<?> createService(@Valid @RequestBody ServiceRequestDTO serviceRequest){
        log.debug("POST request. TechService: {}", serviceRequest);
        URI uri = URI.create("/api/v1/service?id=" +
                serviceTechServ.registerService(serviceRequest));
        return ResponseEntity.created(uri) // HttpStatu2s 201
                .build();
    }

    //Получение услуги по id
    @GetMapping("/{id}")
    public ServiceResponseDTO getServiceById(@PathVariable @Positive int id){
        return null;
    }

    //Получение списка услуг
    @GetMapping
    public List<ServiceResponseDTO> getAllServices(){
        return null;
    }

    //Редактирование услуги
    @PostMapping("/{id}")
    public Long editService(
            @Positive
            @PathVariable
                    ("id") int idSrvice,
            @Valid @RequestBody ServiceRequestDTO serviceRequestDTO){
        return null;
    }
}
