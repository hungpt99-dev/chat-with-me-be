package com.chatme.entity;

import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    private String id;

    private String content;
}
