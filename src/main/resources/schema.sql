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

-- Mock Data for Q&A (only inserts if table is empty)
INSERT INTO questions (id, title, content, author_id, author_name, author_avatar, created_at, views, tags)
SELECT '550e8400-e29b-41d4-a716-446655440000', 'How to center a div in 2024?', 'I know flexbox and grid exist, but what is the modern best practice for centering a div both vertically and horizontally?', 'user1', 'Alice Dev', 'https://i.pravatar.cc/150?u=user1', NOW() - INTERVAL '2 days', 120, ARRAY['css', 'frontend']
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE id = '550e8400-e29b-41d4-a716-446655440000');

INSERT INTO questions (id, title, content, author_id, author_name, author_avatar, created_at, views, tags)
SELECT '550e8400-e29b-41d4-a716-446655440001', 'Best state management for React?', 'Redux, Zustand, Recoil, Context API... I am overwhelmed. What should I use for a medium-sized e-commerce app?', 'user2', 'Bob Smith', 'https://i.pravatar.cc/150?u=user2', NOW() - INTERVAL '5 hours', 45, ARRAY['react', 'state-management']
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE id = '550e8400-e29b-41d4-a716-446655440001');

INSERT INTO questions (id, title, content, author_id, author_name, author_avatar, created_at, views, tags)
SELECT '550e8400-e29b-41d4-a716-446655440002', 'Java vs Go for microservices?', 'I am starting a new backend project. Performance and concurrency are key. Which one should I pick?', 'user3', 'Charlie Lee', 'https://i.pravatar.cc/150?u=user3', NOW() - INTERVAL '1 week', 300, ARRAY['java', 'go', 'backend']
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE id = '550e8400-e29b-41d4-a716-446655440002');

-- Mock Answers
INSERT INTO answers (id, question_id, content, author_id, author_name, author_avatar, created_at)
SELECT '660e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', 'Just use `display: grid; place-items: center;`. It is the shortest and cleanest way now.', 'user3', 'Charlie Lee', 'https://i.pravatar.cc/150?u=user3', NOW() - INTERVAL '1 day'
WHERE NOT EXISTS (SELECT 1 FROM answers WHERE id = '660e8400-e29b-41d4-a716-446655440000');

INSERT INTO answers (id, question_id, content, author_id, author_name, author_avatar, created_at)
SELECT '660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'Zustand is great for simplicity. Redux Toolkit is the industry standard. I would recommend Zustand for medium apps.', 'user1', 'Alice Dev', 'https://i.pravatar.cc/150?u=user1', NOW() - INTERVAL '2 hours'
WHERE NOT EXISTS (SELECT 1 FROM answers WHERE id = '660e8400-e29b-41d4-a716-446655440001');
