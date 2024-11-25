package com.mss.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnlockCodeResponse {

    private int statusCode;
    private String message;
    private String unlockCode;
    private String transactionId;
    private String operator;
    private String shortCode;
    private String msisdn;
    private String keyword;
    private String gameName;

}
