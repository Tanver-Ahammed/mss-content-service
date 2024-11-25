package com.mss.controller;

import com.mss.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    // Endpoint to trigger the fetch and save process
    @GetMapping("/api/fetch-and-save")
    public String fetchAndSaveContents() {
        apiService.fetchAndSaveContents();
        return "Contents have been fetched and saved to the database!";
    }
}
