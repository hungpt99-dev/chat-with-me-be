package com.chatme.repository;

import com.chatme.entity.QuestionReaction;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;

import java.util.List;

@SqlRepository
public interface QuestionReactionRepository extends FastRepository<QuestionReaction, String> {
    @Select("SELECT * FROM question_reactions WHERE question_id = :questionId")
    List<QuestionReaction> findByQuestionId(@Param("questionId") String questionId);
}
