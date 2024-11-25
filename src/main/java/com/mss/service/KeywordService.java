package com.mss.service;

import com.mss.dto.KeywordDto;
import com.mss.entity.Keyword;
import com.mss.repository.KeywordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private ModelMapper modelMapper;

    public KeywordDto saveKeyword(KeywordDto keyword) {
        return this.modelMapper.map(this.keywordRepository.save(
                this.modelMapper.map(keyword, Keyword.class)
        ), KeywordDto.class);
    }

}
