package com.mss.repository;

import com.mss.entity.ChargeConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeConfigRepository extends JpaRepository<ChargeConfig, Long> {

    ChargeConfig findByOperator(String operator);

}
