package com.chatme.repository;

import com.chatme.entity.Answer;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;
import java.util.List;

@SqlRepository
public interface AnswerRepository extends FastRepository<Answer, String> {

    @Select(value = "SELECT * FROM answers WHERE question_id = :questionId ORDER BY created_at ASC LIMIT :limit OFFSET :offset")
    List<Answer> findByQuestionId(@Param("questionId") String questionId, @Param("limit") int limit, @Param("offset") int offset);

    @Select(value = "SELECT COUNT(*) FROM answers WHERE question_id = :questionId")
    long countByQuestionId(@Param("questionId") String questionId);
}
