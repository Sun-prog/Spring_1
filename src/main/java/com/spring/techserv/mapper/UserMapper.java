package com.spring.techserv.mapper;

import com.spring.techserv.constants.BookingStatus;
import com.spring.techserv.dto.AccountRequestDTO;
import com.spring.techserv.dto.BookingRequestDTO;
import com.spring.techserv.dto.BookingResponseDTO;
import com.spring.techserv.entity.ApplicationUser;
import com.spring.techserv.entity.Booking;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public static ApplicationUser mapToEntity(AccountRequestDTO accountRequest) {
        return ApplicationUser.builder()
                .username(accountRequest.username())
                .password(accountRequest.password())
                .build();
    }


}
