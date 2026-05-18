package com.spring.techserv.controller;

import com.spring.techserv.dto.JwtAuthenticationResponse;
import com.spring.techserv.dto.SignInRequestDTO;
import com.spring.techserv.dto.SignUpRequestDTO;
import com.spring.techserv.service.AuthenticationService;
import com.spring.techserv.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService service;


    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequestDTO request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequestDTO request) {
        return authenticationService.signIn(request);
    }

    @Operation(summary = "Регистрация оператора")
    @PostMapping("/sign-up-operator")
    public JwtAuthenticationResponse signUpOperator(@RequestBody @Valid SignUpRequestDTO request) {
        return authenticationService.signUpOperator(request);
    }

    @GetMapping("/get-admin")
    @Operation(summary = "Получить роль ADMIN (для демонстрации)")
    public void getAdmin() {
        service.getAdmin();
    }

}