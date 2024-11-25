package com.mss.repository;

import com.mss.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    // custom query to find keyword by name
    Boolean existsByKeyword(String keyword);

}
