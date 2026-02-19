-- Question Reactions
CREATE TABLE IF NOT EXISTS question_reactions (
    id VARCHAR(255) PRIMARY KEY,
    question_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(100),
    type VARCHAR(20),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    CONSTRAINT uk_question_reaction UNIQUE (question_id, user_id, type)
);

-- Blog Post Reactions
CREATE TABLE IF NOT EXISTS post_reactions (
    id VARCHAR(255) PRIMARY KEY,
    post_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(100),
    type VARCHAR(20),
    FOREIGN KEY (post_id) REFERENCES blog_posts(id),
    CONSTRAINT uk_post_reaction UNIQUE (post_id, user_id, type)
);

-- Comment Reactions (for blog comments)
CREATE TABLE IF NOT EXISTS comment_reactions (
    id VARCHAR(255) PRIMARY KEY,
    comment_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(100),
    type VARCHAR(20),
    FOREIGN KEY (comment_id) REFERENCES blog_comments(id),
    CONSTRAINT uk_comment_reaction UNIQUE (comment_id, user_id, type)
);
