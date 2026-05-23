package com.spring.techserv.service;


import com.spring.techserv.dto.AdvNotification;
import com.spring.techserv.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendNotification(AdvNotification advNotification, User user) {
        // TODO: add http request
        String userEmail = user.getEmail();
        String userName = userService.getCurrentUser().getUsername();

        javaMailSender.send(getSimpleMessage(advNotification.messageText(),
                "Уведомление об изменении бронирования",
                new String[]{userEmail}));
    }


    private SimpleMailMessage getSimpleMessage(String message, String subject, String[] setTo){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(setTo); // кому
        mailMessage.setSubject(subject); // тема письма
        mailMessage.setText(message); // текст письма
        return mailMessage;
    }

}
