-- Enable pgvector extension (idempotent)
CREATE EXTENSION IF NOT EXISTS vector;

-- Create documents table for embeddings
CREATE TABLE IF NOT EXISTS documents (
    id bigserial PRIMARY KEY,
    content text,
    metadata jsonb,
    embedding vector(1536) -- openai-text-embedding-3-small dimension
);

-- Index for faster calling (optional but good practice)
-- CREATE INDEX ON documents USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);
