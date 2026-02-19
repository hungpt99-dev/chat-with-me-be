package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;

@Table("question_reactions")
public class QuestionReaction {

    @Id
    private String id;

    @Column("question_id")
    private String questionId;

    @Column("user_id")
    private String userId;

    private String type;

    public QuestionReaction() {}

    public QuestionReaction(String id, String questionId, String userId, String type) {
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.type = type;
    }

    public static QuestionReactionBuilder builder() {
        return new QuestionReactionBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public static class QuestionReactionBuilder {
        private String id;
        private String questionId;
        private String userId;
        private String type;

        QuestionReactionBuilder() {}

        public QuestionReactionBuilder id(String id) { this.id = id; return this; }
        public QuestionReactionBuilder questionId(String questionId) { this.questionId = questionId; return this; }
        public QuestionReactionBuilder userId(String userId) { this.userId = userId; return this; }
        public QuestionReactionBuilder type(String type) { this.type = type; return this; }

        public QuestionReaction build() {
            return new QuestionReaction(id, questionId, userId, type);
        }
    }
}
