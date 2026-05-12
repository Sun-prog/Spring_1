package com.spring.techserv.controller;




import com.spring.techserv.entity.ApplicationUser;
import com.spring.techserv.entity.Token;
import com.spring.techserv.exception.AccountException;
import com.spring.techserv.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/registration")
    public String registration(ApplicationUser applicationUser) {
        return "account/registration";
    }

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public Token loginAccount(@RequestParam("application_user_username") String username,
                              @RequestParam("application_user_password") String password) {
        try {
            return accountService.loginAccount(username, password);
        } catch (AccountException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //3.1. добавлять/редактировать профиль оператора
    @PostMapping("/registrationUpdateOperator")
    public String createOrUpdateAccountOperator(ApplicationUser user, Model model) {
        try {
            accountService.registrationOperator(user);
            return "redirect:/account/login";
        } catch (AccountException e) {
            model.addAttribute("error", e.getMessage());
            return "account/registration";
        }
    }

    //добавлять/редактировать профиль пользователя
    @PostMapping("/registrationUser")
    public String createAccountUser(ApplicationUser user, Model model) {
        try {
            accountService.registration(user);
            return "redirect:/account/login";
        } catch (AccountException e) {
            model.addAttribute("error", e.getMessage());
            return "account/registration";
        }
    }

}