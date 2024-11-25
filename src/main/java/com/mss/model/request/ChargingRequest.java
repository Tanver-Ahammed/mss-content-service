package com.mss.model.request;

import lombok.Data;

@Data
public class ChargingRequest {

    private String transactionId;
    private String operator;
    private String shortCode;
    private String msisdn;
    private String chargeCode;

}
