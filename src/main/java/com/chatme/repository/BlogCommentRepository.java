package com.chatme.repository;

import com.chatme.entity.BlogComment;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;
import java.util.List;

@SqlRepository
public interface BlogCommentRepository extends FastRepository<BlogComment, String> {
    
    @Select("SELECT * FROM blog_comments WHERE post_id = :postId ORDER BY created_at DESC")
    List<BlogComment> findByPostId(@Param("postId") String postId);
}
