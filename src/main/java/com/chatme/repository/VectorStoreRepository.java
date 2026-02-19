package com.chatme.repository;

import com.chatme.entity.Document;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;

import java.util.List;

@SqlRepository
public interface VectorStoreRepository extends FastRepository<Document, String> {

    @Select("SELECT id, content FROM documents ORDER BY embedding <-> :embedding::vector LIMIT :limit")
    List<Document> searchSimilar(@Param("embedding") String embedding, @Param("limit") int limit);
}
