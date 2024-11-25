package com.mss.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Inbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String transactionId;

    private String operator;

    private String keyword;

    private String shortCode;

    private String msisdn;

    private String sms;

    private String gameName;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
