package com.chatme.repository;

import com.chatme.entity.Question;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.Execute;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;
import java.util.List;

@SqlRepository
public interface QuestionRepository extends FastRepository<Question, String> {

    @Select(value = "SELECT * FROM questions ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    List<Question> findAllNewest(@Param("limit") int limit, @Param("offset") int offset);

    @Select(value = "SELECT * FROM questions ORDER BY views DESC LIMIT :limit OFFSET :offset")
    List<Question> findAllPopular(@Param("limit") int limit, @Param("offset") int offset);

    @Select(value = "SELECT * FROM questions WHERE title ILIKE :search OR content ILIKE :search ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    List<Question> search(@Param("search") String search, @Param("limit") int limit, @Param("offset") int offset);

    @Select(value = "SELECT COUNT(*) FROM questions WHERE title ILIKE :search OR content ILIKE :search")
    Long countSearch(@Param("search") String search);

    @Execute(value = "UPDATE questions SET views = views + 1 WHERE id = :id")
    void incrementViews(@Param("id") String id);
}
