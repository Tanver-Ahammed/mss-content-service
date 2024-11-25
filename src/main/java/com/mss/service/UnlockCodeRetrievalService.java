package com.mss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mss.entity.Inbox;
import com.mss.model.response.ApiResponseWrapper;
import com.mss.model.response.UnlockCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UnlockCodeRetrievalService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public UnlockCodeRetrievalService(RestTemplate restTemplate,
                                      ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ApiResponseWrapper unlockCodeRetrieval(Inbox inbox) throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(inbox);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<UnlockCodeResponse> response;
        try {
            String SERVICE_Retrieval_URL = "http://demo.webmanza.com/a55dbz923ace647v/api/v1.0/services/unlockCode";
            response = restTemplate
                    .exchange(SERVICE_Retrieval_URL, HttpMethod.POST, entity, UnlockCodeResponse.class);

            return new ApiResponseWrapper(response.getBody(), "200");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return new ApiResponseWrapper(null,
                    e.getStatusCode().toString().split(" ")[0]);
        }
    }

}
