package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;

import java.time.Instant;

@Table("blog_comments")
public class BlogComment {

    @Id
    private String id;

    @Column("post_id")
    private String postId;

    @Column("author_name")
    private String authorName;

    private String content;

    @Column("created_at")
    private Instant createdAt;

    public BlogComment() {}

    public BlogComment(String id, String postId, String authorName, String content, Instant createdAt) {
        this.id = id;
        this.postId = postId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static BlogCommentBuilder builder() {
        return new BlogCommentBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static class BlogCommentBuilder {
        private String id;
        private String postId;
        private String authorName;
        private String content;
        private Instant createdAt;

        BlogCommentBuilder() {}

        public BlogCommentBuilder id(String id) { this.id = id; return this; }
        public BlogCommentBuilder postId(String postId) { this.postId = postId; return this; }
        public BlogCommentBuilder authorName(String authorName) { this.authorName = authorName; return this; }
        public BlogCommentBuilder content(String content) { this.content = content; return this; }
        public BlogCommentBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public BlogComment build() {
            return new BlogComment(id, postId, authorName, content, createdAt);
        }
    }
}
