package com.mss.service;

import com.mss.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InboxService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private RestTemplate restTemplate;



}
