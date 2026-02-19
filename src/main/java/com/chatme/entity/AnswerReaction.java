package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("answer_reactions")
public class AnswerReaction {

    @Id
    private String id;

    @Column("answer_id")
    private String answerId;

    @Column("user_id")
    private String userId;

    private String type;

    public AnswerReaction() {}

    public AnswerReaction(String id, String answerId, String userId, String type) {
        this.id = id;
        this.answerId = answerId;
        this.userId = userId;
        this.type = type;
    }

    public static AnswerReactionBuilder builder() {
        return new AnswerReactionBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAnswerId() { return answerId; }
    public void setAnswerId(String answerId) { this.answerId = answerId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public static class AnswerReactionBuilder {
        private String id;
        private String answerId;
        private String userId;
        private String type;

        AnswerReactionBuilder() {}

        public AnswerReactionBuilder id(String id) { this.id = id; return this; }
        public AnswerReactionBuilder answerId(String answerId) { this.answerId = answerId; return this; }
        public AnswerReactionBuilder userId(String userId) { this.userId = userId; return this; }
        public AnswerReactionBuilder type(String type) { this.type = type; return this; }

        public AnswerReaction build() {
            return new AnswerReaction(id, answerId, userId, type);
        }
    }
}
