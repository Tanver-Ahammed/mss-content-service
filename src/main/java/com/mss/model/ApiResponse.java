package com.mss.model;

import com.mss.entity.Content;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponse {

    private int statusCode;
    private String message;
    private int contentCount;
    private List<Content> contents;
}
