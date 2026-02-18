package com.chatme.dto.qa;

import lombok.Builder;

@Builder
public record UserDto(
    String id,
    String name,
    String avatar_url
) {}
