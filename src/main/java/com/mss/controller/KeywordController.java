package com.mss.controller;

import com.mss.dto.KeywordDto;
import com.mss.service.KeywordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/keyword")
public class KeywordController {

    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @PostMapping
    public ResponseEntity<KeywordDto> saveKeyword(@RequestBody KeywordDto keywordDto) {
        return ResponseEntity.ok(keywordService.saveKeyword(keywordDto));
    }
}
