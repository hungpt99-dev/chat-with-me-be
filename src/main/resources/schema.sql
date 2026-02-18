-- Enable pgvector extension (idempotent)
CREATE EXTENSION IF NOT EXISTS vector;

-- Create documents table for embeddings
CREATE TABLE IF NOT EXISTS documents (
    id bigserial PRIMARY KEY,
    content text,
    metadata jsonb,
    embedding vector(1536) -- openai-text-embedding-3-small dimension
);

-- QA Feature Tables

CREATE TABLE IF NOT EXISTS questions (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id VARCHAR(100),
    author_name VARCHAR(100),
    author_avatar VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    views INT DEFAULT 0,
    tags VARCHAR(255) ARRAY -- H2 and Postgres support ARRAY
);

CREATE TABLE IF NOT EXISTS answers (
    id UUID PRIMARY KEY,
    question_id UUID NOT NULL,
    content TEXT NOT NULL,
    author_id VARCHAR(100),
    author_name VARCHAR(100),
    author_avatar VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

CREATE TABLE IF NOT EXISTS answer_reactions (
    id UUID PRIMARY KEY,
    answer_id UUID NOT NULL,
    user_id VARCHAR(100),
    type VARCHAR(20),
    FOREIGN KEY (answer_id) REFERENCES answers(id),
    CONSTRAINT uk_reaction UNIQUE (answer_id, user_id, type)
);
