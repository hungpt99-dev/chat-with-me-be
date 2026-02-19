package com.chatme.repository;

import com.chatme.entity.BlogPostReaction;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;

import java.util.List;

@SqlRepository
public interface BlogPostReactionRepository extends FastRepository<BlogPostReaction, String> {
    @Select("SELECT * FROM post_reactions WHERE post_id = :postId")
    List<BlogPostReaction> findByPostId(@Param("postId") String postId);
}
