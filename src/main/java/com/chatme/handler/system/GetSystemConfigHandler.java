package com.chatme.handler.system;


import com.chatme.entity.SystemConfig;
import com.chatme.repository.SystemConfigRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetSystemConfigHandler implements QueryHandler<GetSystemConfigHandler.Query, List<GetSystemConfigHandler.Response>> {

    public record Query() {}

    public record Response(
        String configKey,
        String configValue,
        String description
    ) {
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String configKey;
            private String configValue;
            private String description;
            public Builder configKey(String v) { this.configKey = v; return this; }
            public Builder configValue(String v) { this.configValue = v; return this; }
            public Builder description(String v) { this.description = v; return this; }
            public Response build() { return new Response(configKey, configValue, description); }
        }
    }

    private final SystemConfigRepository systemConfigRepository;

    @Override
    public List<Response> handle(Query query) {
        return systemConfigRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Response mapToDto(SystemConfig config) {
        return Response.builder()
                .configKey(config.getConfigKey())
                .configValue(config.getConfigValue())
                .description(config.getDescription())
                .build();
    }
}
