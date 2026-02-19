package com.chatme.repository;

import com.chatme.entity.CommentReaction;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;

import java.util.List;

@SqlRepository
public interface CommentReactionRepository extends FastRepository<CommentReaction, String> {
    @Select("SELECT * FROM comment_reactions WHERE comment_id = :commentId")
    List<CommentReaction> findByCommentId(@Param("commentId") String commentId);
}
