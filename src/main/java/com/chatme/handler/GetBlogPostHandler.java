package com.chatme.handler;

import com.chatme.dto.BlogPostDto;
import com.chatme.dto.GetBlogPostQuery;
import com.fast.cqrs.cqrs.QueryHandler;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GetBlogPostHandler implements QueryHandler<GetBlogPostQuery, BlogPostDto> {

    @Override
    public BlogPostDto handle(GetBlogPostQuery query) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource resource = resolver.getResource("classpath:posts/" + query.id() + ".md");
            
            if (resource.exists()) {
                return parsePost(resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BlogPostDto parsePost(Resource resource) throws IOException {
        String content = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
        
        // Simple frontmatter parser
        String[] parts = content.split("---", 3);
        String frontmatter = "";
        String body = content;
        
        if (parts.length >= 3) {
            frontmatter = parts[1];
            body = parts[2];
        }

        String title = extractValue(frontmatter, "title");
        String date = extractValue(frontmatter, "date");
        String author = extractValue(frontmatter, "author");
        String description = extractValue(frontmatter, "description");
        String imageUrl = extractValue(frontmatter, "image");
        String tagsStr = extractValue(frontmatter, "tags");
        List<String> tags = tagsStr.isEmpty() ? Collections.emptyList() : Arrays.asList(tagsStr.replace("[", "").replace("]", "").replace("\"", "").split(","));
        
        // Calculate read time
        int wordCount = body.split("\\s+").length;
        int readTimeMin = (int) Math.ceil(wordCount / 200.0);
        String readTime = readTimeMin + " min read";

        String id = resource.getFilename().replace(".md", "");
        String category = extractValue(frontmatter, "category");

        return new BlogPostDto(
            id,
            title,
            date,
            author != null && !author.isEmpty() ? author : "Admin",
            tags,
            category != null && !category.isEmpty() ? category : "Tech",
            description,
            body.trim(),
            imageUrl,
            readTime,
            date,
            date
        );
    }

     private String extractValue(String frontmatter, String key) {
        String[] lines = frontmatter.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith(key + ":")) {
                String value = line.substring(key.length() + 1).trim();
                // Remove quotes if present
                 if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                    return value.substring(1, value.length() - 1);
                }
                return value;
            }
        }
        return "";
    }
}
