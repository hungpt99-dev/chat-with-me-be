package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("answer_reactions")
public class AnswerReaction {

    @Id
    private String id;

    @Column("answer_id")
    private String answerId;

    @Column("user_id")
    private String userId;

    private String type;
}
