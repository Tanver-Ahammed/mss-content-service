package com.mss.model.response;

import com.mss.entity.Inbox;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContentApiResponse {

    private int statusCode;
    private String message;
    private int contentCount;
    private List<Inbox> contents;
}
