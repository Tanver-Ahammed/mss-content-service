package com.mss.service;

import com.mss.entity.Inbox;
import com.mss.model.ApiResponse;
import com.mss.repository.InboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final InboxRepository inboxRepository;

    private final String API_URL = "http://demo.webmanza.com/a55dbz923ace647v/api/v1.0/services/content";  // Replace with your actual API URL

    @Autowired
    public ApiService(RestTemplate restTemplate, InboxRepository inboxRepository) {
        this.restTemplate = restTemplate;
        this.inboxRepository = inboxRepository;
    }

    // Method to fetch and save contents from external API
    public void fetchAndSaveContents() {
        // Make the GET request to the external API
        ApiResponse apiResponse = restTemplate.getForObject(API_URL, ApiResponse.class);

        // Check if API response is valid
        if (apiResponse != null && apiResponse.getContents() != null) {
            List<Inbox> contents = apiResponse.getContents();
            // Save the fetched contents into the database
            for (Inbox inbox : contents) {
                System.out.println(inbox.toString());
            }
            inboxRepository.saveAll(contents);
        }
    }
}