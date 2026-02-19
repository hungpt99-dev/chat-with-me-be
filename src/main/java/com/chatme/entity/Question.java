package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
    private Timestamp createdAt;

    private int views;

    public Question() {}

    public Question(String id, String title, String content, String authorId, String authorName, String authorAvatar, Timestamp createdAt, int views) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.createdAt = createdAt;
        this.views = views;
    }

    public static QuestionBuilder builder() {
        return new QuestionBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

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

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public static class QuestionBuilder {
        private String id;
        private String title;
        private String content;
        private String authorId;
        private String authorName;
        private String authorAvatar;
        private Timestamp createdAt;
        private int views;

        QuestionBuilder() {}

        public QuestionBuilder id(String id) { this.id = id; return this; }
        public QuestionBuilder title(String title) { this.title = title; return this; }
        public QuestionBuilder content(String content) { this.content = content; return this; }
        public QuestionBuilder authorId(String authorId) { this.authorId = authorId; return this; }
        public QuestionBuilder authorName(String authorName) { this.authorName = authorName; return this; }
        public QuestionBuilder authorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; return this; }
        public QuestionBuilder createdAt(Timestamp createdAt) { this.createdAt = createdAt; return this; }
        public QuestionBuilder views(int views) { this.views = views; return this; }

        public Question build() {
            return new Question(id, title, content, authorId, authorName, authorAvatar, createdAt, views);
        }
    }
}
