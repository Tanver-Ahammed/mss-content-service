package com.mss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mss.entity.ChargeFailureLog;
import com.mss.entity.ChargeSuccessLog;
import com.mss.model.request.ChargingRequest;
import com.mss.model.response.ApiResponseWrapper;
import com.mss.model.response.ChargingResponse;
import com.mss.repository.ChargeFailureLogRepository;
import com.mss.repository.ChargeSuccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class ChargeService {

    private final ChargeSuccessLogRepository chargeSuccessLogRepository;
    private final ChargeFailureLogRepository chargeFailureLogRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChargeService(ChargeSuccessLogRepository chargeSuccessLogRepository,
                         ChargeFailureLogRepository chargeFailureLogRepository,
                         RestTemplate restTemplate,
                         ObjectMapper objectMapper) {
        this.chargeSuccessLogRepository = chargeSuccessLogRepository;
        this.chargeFailureLogRepository = chargeFailureLogRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ApiResponseWrapper chargeCheck(ChargingRequest chargingRequest) throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(chargingRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            String SERVICE_Retrieval_URL = "https://demo.webmanza.com/a55dbz923ace647v/api/v1.0/services/charge";
            ResponseEntity<ChargingResponse> response = restTemplate
                    .exchange(SERVICE_Retrieval_URL, HttpMethod.POST, entity, ChargingResponse.class);

            return new ApiResponseWrapper(response.getBody(), "200");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return new ApiResponseWrapper(null,
                    e.getStatusCode().toString().split(" ")[0]);
        }
    }

    public void saveChargeSuccessLog(ChargeSuccessLog chargeSuccessLog) {
        // Save the charge success log to the database
        LocalDateTime now = LocalDateTime.now();
        chargeSuccessLog.setCreatedAt(now);
        chargeSuccessLog.setUpdatedAt(now);
        this.chargeSuccessLogRepository.save(chargeSuccessLog);
    }

    public void saveChargeFailureLog(ChargeFailureLog chargeFailureLog) {
        // Save the charge failure log to the database
        LocalDateTime now = LocalDateTime.now();
        chargeFailureLog.setCreatedAt(now);
        chargeFailureLog.setUpdatedAt(now);
        this.chargeFailureLogRepository.save(chargeFailureLog);
    }
}
