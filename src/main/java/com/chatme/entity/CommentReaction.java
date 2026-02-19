package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;

@Table("comment_reactions")
public class CommentReaction {

    @Id
    private String id;

    @Column("comment_id")
    private String commentId;

    @Column("user_id")
    private String userId;

    private String type;

    public CommentReaction() {}

    public CommentReaction(String id, String commentId, String userId, String type) {
        this.id = id;
        this.commentId = commentId;
        this.userId = userId;
        this.type = type;
    }

    public static CommentReactionBuilder builder() {
        return new CommentReactionBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCommentId() { return commentId; }
    public void setCommentId(String commentId) { this.commentId = commentId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public static class CommentReactionBuilder {
        private String id;
        private String commentId;
        private String userId;
        private String type;

        CommentReactionBuilder() {}

        public CommentReactionBuilder id(String id) { this.id = id; return this; }
        public CommentReactionBuilder commentId(String commentId) { this.commentId = commentId; return this; }
        public CommentReactionBuilder userId(String userId) { this.userId = userId; return this; }
        public CommentReactionBuilder type(String type) { this.type = type; return this; }

        public CommentReaction build() {
            return new CommentReaction(id, commentId, userId, type);
        }
    }
}
