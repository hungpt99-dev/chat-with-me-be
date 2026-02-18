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
@Table("answers")
public class Answer {

    @Id
    private String id;

    @Column("question_id")
    private String questionId;

    private String content;

    @Column("author_id")
    private String authorId;

    @Column("author_name")
    private String authorName;

    @Column("author_avatar")
    private String authorAvatar;

    @Column("created_at")
    private Instant createdAt;
}
