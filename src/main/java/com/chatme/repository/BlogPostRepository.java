package com.chatme.repository;

import com.chatme.entity.BlogPost;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;

@SqlRepository
public interface BlogPostRepository extends FastRepository<BlogPost, String> {
    
}
