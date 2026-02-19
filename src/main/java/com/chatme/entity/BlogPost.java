package com.chatme.entity;

import com.fast.cqrs.sql.repository.Column;
import com.fast.cqrs.sql.repository.Id;
import com.fast.cqrs.sql.repository.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Table("blog_posts")
public class BlogPost {

    @Id
    private String id;

    private String title;
    private String body;
    private String description;
    private String author;
    private String category;
    
    @Column("image_url")
    private String imageUrl;
    
    @Column("read_time")
    private String readTime;
    
    @Column("created_date")
    private String createdDate;
    
    @Column("created_at")
    private Timestamp createdAt;
    
    @Column("updated_at")
    private Timestamp updatedAt;

    public BlogPost() {}

    public BlogPost(String id, String title, String body, String description, String author, String category, String imageUrl, String readTime, String createdDate, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.description = description;
        this.author = author;
        this.category = category;
        this.imageUrl = imageUrl;
        this.readTime = readTime;
        this.createdDate = createdDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BlogPostBuilder builder() {
        return new BlogPostBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getReadTime() { return readTime; }
    public void setReadTime(String readTime) { this.readTime = readTime; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public static class BlogPostBuilder {
        private String id;
        private String title;
        private String body;
        private String description;
        private String author;
        private String category;
        private String imageUrl;
        private String readTime;
        private String createdDate;
        private Timestamp createdAt;
        private Timestamp updatedAt;

        BlogPostBuilder() {}

        public BlogPostBuilder id(String id) { this.id = id; return this; }
        public BlogPostBuilder title(String title) { this.title = title; return this; }
        public BlogPostBuilder body(String body) { this.body = body; return this; }
        public BlogPostBuilder description(String description) { this.description = description; return this; }
        public BlogPostBuilder author(String author) { this.author = author; return this; }
        public BlogPostBuilder category(String category) { this.category = category; return this; }
        public BlogPostBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public BlogPostBuilder readTime(String readTime) { this.readTime = readTime; return this; }
        public BlogPostBuilder createdDate(String createdDate) { this.createdDate = createdDate; return this; }
        public BlogPostBuilder createdAt(Timestamp createdAt) { this.createdAt = createdAt; return this; }
        public BlogPostBuilder updatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; return this; }

        public BlogPost build() {
            return new BlogPost(id, title, body, description, author, category, imageUrl, readTime, createdDate, createdAt, updatedAt);
        }
    }
}
