package com.mss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mss.entity.Inbox;
import com.mss.model.request.ChargingRequest;
import com.mss.model.response.ApiResponseWrapper;
import com.mss.model.response.ChargingResponse;
import com.mss.model.response.ContentApiResponse;
import com.mss.model.response.UnlockCodeResponse;
import com.mss.repository.InboxRepository;
import com.mss.repository.KeywordRepository;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;
    private final KeywordRepository keywordRepository;

    @Autowired
    public ApiService(RestTemplate restTemplate, InboxRepository inboxRepository, ObjectMapper objectMapper, KeywordRepository keywordRepository) {
        this.restTemplate = restTemplate;
        this.inboxRepository = inboxRepository;
        this.keywordRepository = keywordRepository;
        this.objectMapper = objectMapper;
    }

    public void fetchAndSaveContents() {
        // Define the API URL
        String API_URL = "http://demo.webmanza.com/a55dbz923ace647v/api/v1.0/services/content";
        ContentApiResponse apiResponse = restTemplate.getForObject(API_URL, ContentApiResponse.class);

        // Check if API response is valid
        if (apiResponse != null && apiResponse.getContents() != null) {
            List<Inbox> contents = apiResponse.getContents();
            int count = 0;

            Semaphore semaphore = new Semaphore(8); // Limit to 8 concurrent tasks

            try (var executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())) {
                for (Inbox inbox : contents) {
                    ++count;

                    executor.execute(() -> {
                        try {
                            semaphore.acquire();

                            System.out.println(inbox.toString());
                            inbox.setStatus("N");

                            LocalDateTime now = LocalDateTime.now();
                            inbox.setCreatedAt(now);
                            inbox.setUpdatedAt(now);

                            String[] data = inbox.getSms().split(" ");
                            inbox.setKeyword(data[0]);
                            inbox.setGameName(data[1]);

                            Inbox savedInbox = this.inboxRepository.save(inbox);

                            boolean isExistKeyword = this.keywordRepository.existsByKeyword(savedInbox.getKeyword());
                            if (isExistKeyword) {
                                ApiResponseWrapper apiResponseWrapper = unlockCodeRetrieval(inbox);
                                System.err.println(apiResponseWrapper.getStatusCode() +
                                        "\n...................................................");
                                if (apiResponseWrapper.getStatusCode().equals("200")) {
                                    UnlockCodeResponse unlockCodeResponse = (UnlockCodeResponse) apiResponseWrapper.getResponse();
                                    System.out.println(unlockCodeResponse.toString());
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            semaphore.release();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.err.println("Total contents processed: " + count);
        }
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

    public ApiResponseWrapper chargeCheck(ChargingRequest chargingRequest) throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(chargingRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            String SERVICE_Retrieval_URL = "http://demo.webmanza.com/a55dbz923ace647v/api/v1.0/services/charge";
            ResponseEntity<ChargingResponse> response = restTemplate
                    .exchange(SERVICE_Retrieval_URL, HttpMethod.POST, entity, ChargingResponse.class);

            return new ApiResponseWrapper(response.getBody(), "200");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return new ApiResponseWrapper(null,
                    e.getStatusCode().toString().split(" ")[0]);
        }
    }

}
