package com.mss.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeFailureLog {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    private String transactionId;

    private String operator;

    private String shortCode;

    private String msisdn;

    private String keyword;

    private String gameName;

    private String statusCode;

    private String message;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long smsId;

}
