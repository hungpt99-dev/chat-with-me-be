package com.chatme.dto;

import java.time.LocalDate;
import java.util.List;

public record BlogPostDto(
    String id,
    String title,
    String date,
    String author,
    List<String> tags,
    String category,
    String description,
    String content, // HTML or Markdown content
    String imageUrl,
    String readTime,
    String created_at,
    String updated_at
) {}
