package com.mss.service;

import com.mss.entity.ChargeFailureLog;
import com.mss.entity.ChargeSuccessLog;
import com.mss.entity.Inbox;
import com.mss.model.request.ChargingRequest;
import com.mss.model.response.ApiResponseWrapper;
import com.mss.model.response.ChargingResponse;
import com.mss.model.response.ContentApiResponse;
import com.mss.model.response.UnlockCodeResponse;
import com.mss.repository.ChargeConfigRepository;
import com.mss.repository.InboxRepository;
import com.mss.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service
public class ContentConsumerService {

    private final RestTemplate restTemplate;
    private final InboxRepository inboxRepository;
    private final KeywordRepository keywordRepository;
    private final ChargeConfigRepository chargeConfigRepository;
    private final UnlockCodeRetrievalService unlockService;
    private final ChargeService chargeService;

    @Autowired
    public ContentConsumerService(RestTemplate restTemplate,
                                  InboxRepository inboxRepository,
                                  KeywordRepository keywordRepository,
                                  ChargeConfigRepository chargeConfigRepository,
                                  UnlockCodeRetrievalService unlockService,
                                  ChargeService chargeService) {
        this.restTemplate = restTemplate;
        this.inboxRepository = inboxRepository;
        this.keywordRepository = keywordRepository;
        this.chargeConfigRepository = chargeConfigRepository;
        this.unlockService = unlockService;
        this.chargeService = chargeService;
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
                                ApiResponseWrapper apiResponseWrapper = this.unlockService.unlockCodeRetrieval(inbox);
                                System.err.println(apiResponseWrapper.getStatusCode() +
                                        "\n...................................................");
                                // unlock code retrieval
                                if (apiResponseWrapper.getStatusCode().equals("200")) {
                                    UnlockCodeResponse unlockCodeResponse = (UnlockCodeResponse) apiResponseWrapper.getResponse();
                                    System.out.println(unlockCodeResponse.toString());
                                    // charge check
                                    ChargingRequest chargingRequest = new ChargingRequest();
                                    chargingRequest.setTransactionId(unlockCodeResponse.getTransactionId());
                                    chargingRequest.setOperator(unlockCodeResponse.getOperator());
                                    chargingRequest.setShortCode(unlockCodeResponse.getShortCode());
                                    chargingRequest.setMsisdn(unlockCodeResponse.getMsisdn());
                                    chargingRequest.setChargeCode(this.chargeConfigRepository
                                            .findByOperator(unlockCodeResponse.getOperator()).getChargeCode());
                                    ApiResponseWrapper chargeCheckResponse = this.chargeService.chargeCheck(chargingRequest);
                                    System.err.println(chargeCheckResponse.getStatusCode() +
                                            "\n...................................................");
                                    if (chargeCheckResponse.getStatusCode().equals("200")) {
                                        ChargingResponse chargingResponse = (ChargingResponse) chargeCheckResponse.getResponse();
                                        System.out.println(chargingResponse.toString());
                                        savedInbox.setStatus("S");
                                        savedInbox.setUpdatedAt(LocalDateTime.now());
                                        this.inboxRepository.save(savedInbox);
                                        ChargeSuccessLog chargeSuccessLog = new ChargeSuccessLog();
                                        chargeSuccessLog.setTransactionId(savedInbox.getTransactionId());
                                        chargeSuccessLog.setOperator(savedInbox.getOperator());
                                        chargeSuccessLog.setShortCode(savedInbox.getShortCode());
                                        chargeSuccessLog.setMsisdn(savedInbox.getMsisdn());
                                        chargeSuccessLog.setKeyword(savedInbox.getKeyword());
                                        chargeSuccessLog.setGameName(savedInbox.getGameName());
                                        chargeSuccessLog.setSmsId(savedInbox.getId());
                                        this.chargeService.saveChargeSuccessLog(chargeSuccessLog);
                                    } else {
                                        savedInbox.setStatus("F");
                                        savedInbox.setUpdatedAt(LocalDateTime.now());
                                        this.inboxRepository.save(savedInbox);
                                        ChargeFailureLog chargeFailureLog = new ChargeFailureLog();
                                        chargeFailureLog.setTransactionId(savedInbox.getTransactionId());
                                        chargeFailureLog.setOperator(savedInbox.getOperator());
                                        chargeFailureLog.setShortCode(savedInbox.getShortCode());
                                        chargeFailureLog.setMsisdn(savedInbox.getMsisdn());
                                        chargeFailureLog.setKeyword(savedInbox.getKeyword());
                                        chargeFailureLog.setGameName(savedInbox.getGameName());
                                        chargeFailureLog.setSmsId(savedInbox.getId());
                                        chargeFailureLog.setStatusCode(chargeCheckResponse.getStatusCode());
                                        this.chargeService.saveChargeFailureLog(chargeFailureLog);
                                    }
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

}
