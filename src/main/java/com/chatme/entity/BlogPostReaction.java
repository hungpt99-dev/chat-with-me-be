package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;

@Table("post_reactions")
public class BlogPostReaction {

    @Id
    private String id;

    @Column("post_id")
    private String postId;

    @Column("user_id")
    private String userId;

    private String type;

    public BlogPostReaction() {}

    public BlogPostReaction(String id, String postId, String userId, String type) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.type = type;
    }

    public static BlogPostReactionBuilder builder() {
        return new BlogPostReactionBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public static class BlogPostReactionBuilder {
        private String id;
        private String postId;
        private String userId;
        private String type;

        BlogPostReactionBuilder() {}

        public BlogPostReactionBuilder id(String id) { this.id = id; return this; }
        public BlogPostReactionBuilder postId(String postId) { this.postId = postId; return this; }
        public BlogPostReactionBuilder userId(String userId) { this.userId = userId; return this; }
        public BlogPostReactionBuilder type(String type) { this.type = type; return this; }

        public BlogPostReaction build() {
            return new BlogPostReaction(id, postId, userId, type);
        }
    }
}
