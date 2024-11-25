package com.mss.repository;

import com.mss.entity.ChargeFailureLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeFailureLogRepository extends JpaRepository<ChargeFailureLog, Long> {
}
