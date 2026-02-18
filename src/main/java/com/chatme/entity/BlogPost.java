package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("blog_posts")
public class BlogPost {

    @Id
    private String id;

    private String title;
    private String body;
    private String description;
    private String author;
    private String category;
    
    @Column("image_url")
    private String imageUrl;
    
    @Column("read_time")
    private String readTime;
    
    @Column("created_date")
    private String createdDate;
    
    @Column("created_at")
    private Instant createdAt;
    
    @Column("updated_at")
    private Instant updatedAt;
    
    private String[] tags;
}
