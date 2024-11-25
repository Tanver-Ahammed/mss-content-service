package com.mss.model.response;

import lombok.Data;

@Data
public class ChargingResponse {

    private int statusCode;
    private String message;
    private String transactionId;
    private String operator;
    private String shortCode;
    private String msisdn;
    private String chargeCode;

}
