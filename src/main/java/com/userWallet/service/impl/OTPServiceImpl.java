package com.userWallet.service.impl;

import com.userWallet.dto.APIResponse;
import com.userWallet.dto.EmailOtpRequest;
import com.userWallet.dto.OtpVerificationDto;
import com.userWallet.entities.EmailOtp;
import com.userWallet.entities.OTP;
import com.userWallet.repository.EmailOtpRepository;
import com.userWallet.repository.OTPRepository;
import com.userWallet.service.OTPService;
import com.userWallet.utility.OtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class OTPServiceImpl implements OTPService {


    @Autowired
    private OTPRepository otpRepo;

    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailOtpRepository otpRepository;
    @Autowired
    private JavaMailSender mailSender;
//    private PasswordEncoder passwordEncoder;

    @Override
    public APIResponse<String> generateOTP(String mobile) {
        log.info("Received OTP generation request for mobile/mobile: {}", mobile);

        String otp = otpUtil.generateRandomOtp();
        log.debug("Generated OTP: {}", otp);

        OTP otpEntity = new OTP(mobile, otp, LocalDateTime.now().plusMinutes(5));
        otpRepo.save(otpEntity);
        log.info("OTP saved in database for mobile: {}", mobile);

        boolean isSent = otpUtil.sendOtpSms(mobile, otp);

        if (isSent) {
            log.info("OTP sent successfully to mobile/mobile: {}", mobile);
            return new APIResponse<>("SUCCESS", "OTP sent successfully", otp);
        } else {
            log.error("Failed to send OTP to mobile/mobile: {}", mobile);
            return new APIResponse<>("FAILED", "OTP sending failed", null);
        }
    }

    @Override
    public APIResponse<Object> verifyOTP(String mobile, String otp) {
        log.info("Verifying OTP for mobile: {}", mobile);

        Optional<OTP> optionalOtp = otpRepo.findTopByMobileOrderByExpirationTimeDesc(mobile);

        if (optionalOtp.isEmpty()) {
            log.warn("No OTP found for mobile: {}", mobile);
            return new APIResponse<>("FAILED", "No OTP found for user", null);
        }

        OTP storedOtp = optionalOtp.get();

        if (!storedOtp.getOtpCode().equals(otp)) {
            log.warn("Invalid OTP entered for mobile: {}", mobile);
            return new APIResponse<>("FAILED", "Invalid OTP", null);
        }

        if (LocalDateTime.now().isAfter(storedOtp.getExpirationTime())) {
            log.warn("Expired OTP for mobile: {}", mobile);
            return new APIResponse<>("FAILED", "OTP expired", null);
        }

        log.info("OTP verified successfully for mobile: {}", mobile);

        // You can optionally return user/session data in `data`
        return new APIResponse<>("SUCCESS", "OTP verified successfully", Map.of("mobile", mobile));
    }
    // Generate and send OTP
    @Override
    public APIResponse<String> sendOtp(EmailOtpRequest request) {
        log.info("Received OTP request for email: {}", request.getEmail());

        String otp = String.format("%06d", new Random().nextInt(999999));
        log.debug("Generated OTP: {}", otp);

        EmailOtp otpRecord = otpRepository.findByEmail(request.getEmail())
                .orElse(new EmailOtp());

        otpRecord.setEmail(request.getEmail());
        otpRecord.setOtpCode(otp); // plain OTP for demo
        otpRecord.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpRecord);

        sendOtpEmail(request.getEmail(), otp);
        log.info("OTP sent and saved for email: {}", request.getEmail());

        return new APIResponse<>("SUCCESS", "OTP sent to email", otp); // Optional: remove OTP from response in production
    }

    @Override
    public APIResponse<String> verifyOtp(OtpVerificationDto request) {
        log.info("Verifying OTP for email: {}", request.getEmail());

        EmailOtp otpRecord = otpRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found for email"));

        if (LocalDateTime.now().isAfter(otpRecord.getExpirationTime())) {
            log.warn("OTP expired for email: {}", request.getEmail());
            return new APIResponse<>("FAILED", "OTP expired", null);
        }

        if (!otpRecord.getOtpCode().equals(request.getOtp())) {
            log.warn("Invalid OTP entered for email: {}", request.getEmail());
            return new APIResponse<>("FAILED", "Invalid OTP", null);
        }

        log.info("OTP verified successfully for email: {}", request.getEmail());
        return new APIResponse<>("SUCCESS", "OTP verified", null);
    }

    private void sendOtpEmail(String toEmail, String otp) {
        log.info("Sending OTP email to: {}", toEmail);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + "\nValid for 5 minutes");
        mailSender.send(message);
        log.info("OTP email sent to: {}", toEmail);
    }
}