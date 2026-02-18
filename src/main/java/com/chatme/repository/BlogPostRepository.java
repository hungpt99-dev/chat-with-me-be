package com.chatme.repository;

import com.chatme.entity.BlogPost;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;
import com.fast.cqrs.sql.annotation.Select;
import java.util.List;

@SqlRepository
public interface BlogPostRepository extends FastRepository<BlogPost, String> {
    
    @Select("SELECT * FROM blog_posts ORDER BY created_at DESC")
    List<BlogPost> findAll();
}
