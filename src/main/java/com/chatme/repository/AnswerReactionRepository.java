package com.chatme.repository;

import com.chatme.entity.AnswerReaction;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.Execute;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;
import java.util.List;

@SqlRepository
public interface AnswerReactionRepository extends FastRepository<AnswerReaction, String> {

    @Select("SELECT * FROM answer_reactions WHERE answer_id = :answerId")
    List<AnswerReaction> findByAnswerId(@Param("answerId") String answerId);

    @Select("SELECT * FROM answer_reactions WHERE answer_id = :answerId AND user_id = :userId AND type = :type")
    AnswerReaction findByUniqueKey(@Param("answerId") String answerId, @Param("userId") String userId, @Param("type") String type);

    @Execute("DELETE FROM answer_reactions WHERE answer_id = :answerId AND user_id = :userId AND type = :type")
    void deleteByUniqueKey(@Param("answerId") String answerId, @Param("userId") String userId, @Param("type") String type);
}
