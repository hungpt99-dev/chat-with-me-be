package com.chatme.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VectorStoreRepository {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public List<String> searchSimilar(float[] embedding, int limit) {
        String embeddingStr = Arrays.toString(embedding);
        String sql = "SELECT content FROM documents ORDER BY embedding <-> ?::vector LIMIT ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getString("content"),
                embeddingStr, limit);
    }
}
