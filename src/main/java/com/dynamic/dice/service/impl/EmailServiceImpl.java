package com.dynamic.dice.service.impl;

import com.dynamic.dice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtp(String toEmail, String otp, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Dynamic Dice OTP Verification");
        mail.setText(message + "\nYour OTP: " + otp);
        mailSender.send(mail);
    }
}

