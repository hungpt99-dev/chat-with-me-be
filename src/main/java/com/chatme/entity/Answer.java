package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
    private Timestamp createdAt;

    public Answer() {}

    public Answer(String id, String questionId, String content, String authorId, String authorName, String authorAvatar, Timestamp createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.createdAt = createdAt;
    }

    public static AnswerBuilder builder() {
        return new AnswerBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public static class AnswerBuilder {
        private String id;
        private String questionId;
        private String content;
        private String authorId;
        private String authorName;
        private String authorAvatar;
        private Timestamp createdAt;

        AnswerBuilder() {}

        public AnswerBuilder id(String id) { this.id = id; return this; }
        public AnswerBuilder questionId(String questionId) { this.questionId = questionId; return this; }
        public AnswerBuilder content(String content) { this.content = content; return this; }
        public AnswerBuilder authorId(String authorId) { this.authorId = authorId; return this; }
        public AnswerBuilder authorName(String authorName) { this.authorName = authorName; return this; }
        public AnswerBuilder authorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; return this; }
        public AnswerBuilder createdAt(Timestamp createdAt) { this.createdAt = createdAt; return this; }

        public Answer build() {
            return new Answer(id, questionId, content, authorId, authorName, authorAvatar, createdAt);
        }
    }
}
