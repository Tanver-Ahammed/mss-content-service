package com.mss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeywordDto {

    private long id;

    private String keyword;

    private String createdAt;

    private String updatedAt;

}
