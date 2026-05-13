package com.spring.techserv.controller;




import com.spring.techserv.dto.AccountRequestDTO;
import com.spring.techserv.dto.BookingRequestDTO;
import com.spring.techserv.entity.ApplicationUser;
import com.spring.techserv.entity.Token;
import com.spring.techserv.exception.AccountException;
import com.spring.techserv.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


   /* //@ResponseBody
    @PostMapping("/login")
    public Token loginAccount(@RequestParam("application_user_username") String username,
                              @RequestParam("application_user_password") String password) {
        try {
            return accountService.loginAccount(username, password);
        } catch (AccountException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }*/

    //3.1. добавлять/редактировать профиль оператора
    @PostMapping("/registrationUpdateOperator")
    public String createOrUpdateAccountOperator(@RequestBody ApplicationUser user) {
       /* try {
            accountService.registrationOperator(user);
           return "redirect:/account/login";
        } catch (AccountException e) {
            model.addAttribute("error", e.getMessage());
            return "templates.account/registration";
        }*/
        return null;    }

    //добавлять/редактировать профиль пользователя
    @PostMapping("/registrationUser")
    public String createAccountUser(@Valid @RequestBody AccountRequestDTO user) {
        try {
            System.out.println("создали");

            accountService.registration(user);
            return "created";

        } catch (AccountException e) {
            System.out.println("ошибка");

        }
        return null;
    }

}