package com.chatme.dto;

import com.fast.cqrs.cqrs.QueryType;

public record GetBlogPostQuery(String id) implements QueryType {}
