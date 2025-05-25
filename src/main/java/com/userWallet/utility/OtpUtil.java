package com.userWallet.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
@Slf4j
@Component
public class OtpUtil {


    @Value("${msg91.auth-key}")
    private String authKey;

    @Value("${msg91.flow-id}")
    private String flowId;

    @Value("${msg91.sender-id}")
    private String senderId;

    @Value("${msg91.api-url}")
    private String apiUrl;

    public String generateRandomOtp() {
        String otp = String.format("%06d", new Random().nextInt(999999));
        log.debug("Generated OTP: {}", otp);
        return otp;
    }

    public boolean sendOtpSms(String mobile, String otp) {
        try {
            log.info("Sending OTP to mobile: {}", mobile);

            String jsonPayload = "{\n" +
                    "  \"flow_id\": \"" + flowId + "\",\n" +
                    "  \"sender\": \"" + senderId + "\",\n" +
                    "  \"mobiles\": \"" + mobile + "\",\n" +
                    "  \"otp\": \"" + otp + "\"\n" +
                    "}";

            log.debug("OTP Payload: {}", jsonPayload);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("authkey", authKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("SMS API Response Code: {}", response.statusCode());
            log.debug("SMS API Response Body: {}", response.body());

            if (response.statusCode() == 200) {
                log.info("OTP sent successfully to mobile: {}", mobile);
                return true;
            } else {
                log.warn("Failed to send OTP. Response code: {}", response.statusCode());
                return false;
            }

        } catch (Exception e) {
            log.error("Exception while sending OTP to {}: {}", mobile, e.getMessage(), e);
            return false;
        }
    }
}