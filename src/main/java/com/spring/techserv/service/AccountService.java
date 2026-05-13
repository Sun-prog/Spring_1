package com.spring.techserv.service;


import com.nimbusds.jose.JOSEException;
import com.spring.techserv.dto.AccountRequestDTO;
import com.spring.techserv.entity.ApplicationUser;
import com.spring.techserv.entity.Booking;
import com.spring.techserv.entity.Token;
import com.spring.techserv.entity.UserRole;
import com.spring.techserv.exception.AccountException;
import com.spring.techserv.mapper.BookingMapper;
import com.spring.techserv.mapper.UserMapper;
import com.spring.techserv.repository.ApplicationUserRepository;
import com.spring.techserv.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final ApplicationUserRepository applicationUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper ;

    public AccountService(ApplicationUserRepository applicationUserRepository,
                          UserRoleRepository userRoleRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager) {
        this.applicationUserRepository = applicationUserRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userMapper =  new UserMapper();
    }

    public void registration(AccountRequestDTO accountRequestDTO) throws AccountException {
        ApplicationUser user = userMapper.mapToEntity(accountRequestDTO);
        if (applicationUserRepository.existsByUsername(user.getUsername())) {
            throw new AccountException("Username is already taken");
        }
        userRoleRepository.findByRoleType(UserRole.RoleType.ROLE_USER)
                .ifPresentOrElse(user::setRole,
                        () -> {
                            UserRole userRole = new UserRole();
                            userRole.setRoleType(UserRole.RoleType.ROLE_USER);
                            user.setRole(userRole);
                            userRoleRepository.save(userRole);
                            System.out.println("назначили роль"+userRole.getRoleType().name());
                        }
                );
        user.setPassword(user.getPassword());
       // user.setPassword(passwordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
        //return user.getId();
    }

    public void registrationOperator(ApplicationUser user) throws AccountException {

        userRoleRepository.findByRoleType(UserRole.RoleType.ROLE_OPERATOR)
                .ifPresentOrElse(user::setRole,
                        () -> {
                            UserRole userRole = new UserRole();
                            userRole.setRoleType(UserRole.RoleType.ROLE_OPERATOR);
                            user.setRole(userRole);
                            userRoleRepository.save(userRole);
                        }
                );
        user.setPassword(user.getPassword());
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
    }

    public Token loginAccount(String username, String password) throws AccountException {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Token token = new Token();
        /*try {
            token.setToken(jwtSecurityService.generateToken((UserDetails) authentication.getPrincipal()));
            token.setRefreshToken(jwtSecurityService.generateRefreshToken());
        } catch (JOSEException e) {
            throw new AccountException("Token cannot ne created: " + e.getMessage());
        }*/
        return token;
    }
}