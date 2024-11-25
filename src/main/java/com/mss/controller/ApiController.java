package com.mss.controller;

import com.mss.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    // Endpoint to trigger the fetch and save process
    @GetMapping("/fetch-and-save/content")
    public String fetchAndSaveContents() {
        long startTime = System.currentTimeMillis();
        apiService.fetchAndSaveContents();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
        return "Contents have been fetched and saved to the database!";
    }
}

//Time taken: 1806ms