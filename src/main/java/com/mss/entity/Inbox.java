package com.mss.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    private UUID transactionId;

    private String transactor;

    private String operator;

    private String keyword;

    private String shortCode;

    private String msisdn;

    private String sms;

    private String gameName;

    private String status;

    private String createdAt;

    private String updatedAt;

}
