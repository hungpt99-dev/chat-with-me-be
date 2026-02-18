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
@Table("questions")
public class Question {

    @Id
    private String id;

    private String title;
    private String content;

    @Column("author_id")
    private String authorId;

    @Column("author_name")
    private String authorName;

    @Column("author_avatar")
    private String authorAvatar;

    @Column("created_at")
    private Instant createdAt;

    private int views;
    
    private String[] tags; 
}
