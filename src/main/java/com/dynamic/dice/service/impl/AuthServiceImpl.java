package com.dynamic.dice.service.impl;

import com.dynamic.dice.dto.AdminOtpVerifyRequest;
import com.dynamic.dice.dto.LoginRequest;
import com.dynamic.dice.dto.LoginResponse;
import com.dynamic.dice.dto.UserOtpVerifyRequest;
import com.dynamic.dice.enums.Role;
import com.dynamic.dice.exception.BusinessException;
import com.dynamic.dice.model.OtpRequest;
import com.dynamic.dice.model.User;
import com.dynamic.dice.repository.OtpRepository;
import com.dynamic.dice.repository.UserRepository;
import com.dynamic.dice.service.AuthService;
import com.dynamic.dice.service.EmailService;
import com.dynamic.dice.utils.JwtUtil;
import com.dynamic.dice.utils.OtpUtil;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void requestAdminOtp(String email) {

        if (userRepository.findByUsername(email).isPresent()) {
            throw new BusinessException("Admin already registered");
        }

        String otp = OtpUtil.generateOtp();

        otpRepository.save(
                OtpRequest.builder()
                        .email(email)
                        .otp(otp)
                        .purpose("ADMIN_REGISTRATION")
                        .expiresAt(Instant.now().plusSeconds(300))
                        .verified(false)
                        .build()
        );

        emailService.sendOtp(
                email,
                otp,
                "OTP for Admin registration"
        );
    }


    @Override
    public void verifyAdminOtpAndRegister(AdminOtpVerifyRequest request) {

        OtpRequest otp = otpRepository.findValidOtp(
                request.getEmail(),
                request.getOtp(),
                "ADMIN_REGISTRATION",
                Instant.now()
        ).orElseThrow(() -> new BusinessException("Invalid or expired OTP"));

        User admin = User.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .status("ACTIVE")
                .createdAt(Instant.now())
                .build();

        userRepository.save(admin);

        otp.setVerified(true);
        otpRepository.save(otp);
    }

    @Override
    public void requestUserOtp(String userEmail, String adminEmail) {

        User admin = userRepository.findByUsername(adminEmail)
                .filter(u -> u.getRole() == Role.ADMIN)
                .orElseThrow(() -> new BusinessException("Admin not found"));

        if (userRepository.findByUsername(userEmail).isPresent()) {
            throw new BusinessException("User already registered");
        }

        String otp = OtpUtil.generateOtp();

        otpRepository.save(
                OtpRequest.builder()
                        .email(adminEmail)
                        .otp(otp)
                        .purpose("USER_REGISTRATION")
                        .expiresAt(Instant.now().plusSeconds(300))
                        .verified(false)
                        .metadata(Map.of("userEmail", userEmail))
                        .build()
        );

        emailService.sendOtp(
                adminEmail,
                otp,
                "OTP to approve user registration for: " + userEmail
        );
    }

    @Override
    public void verifyUserOtpAndRegister(UserOtpVerifyRequest request) {

        OtpRequest otp = otpRepository.findValidOtp(
                request.getAdminEmail(),
                request.getOtp(),
                "USER_REGISTRATION",
                Instant.now()
        ).orElseThrow(() -> new BusinessException("Invalid or expired OTP"));

        String userEmail = otp.getMetadata().get("userEmail");

        User admin = userRepository.findByUsername(request.getAdminEmail())
                .orElseThrow(() -> new BusinessException("Admin not found"));

        User user = User.builder()
                .username(userEmail)
                .password(passwordEncoder.encode(request.getUserPassword()))
                .role(Role.USER)
                .adminId(admin.getId())
                .status("ACTIVE")
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);

        otp.setVerified(true);
        otpRepository.save(otp);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }

        // avoid NPE from autounboxing a null Long
        String adminIdForToken = user.getAdminId() == null ? UUID.randomUUID().toString() : user.getAdminId();

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                adminIdForToken
        );

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .userId(user.getId())
                .adminId(user.getAdminId())
                .build();
    }

}
