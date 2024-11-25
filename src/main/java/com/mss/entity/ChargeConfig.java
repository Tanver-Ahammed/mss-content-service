package com.mss.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeConfig {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String operator;

    private String chargeCode;

    private String createdAt;

    private String updatedAt;

    public ChargeConfig(String operator, String chargeCode, String createdAt, String updatedAt) {
        this.operator = operator;
        this.chargeCode = chargeCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
