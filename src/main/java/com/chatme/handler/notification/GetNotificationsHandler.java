package com.chatme.handler.notification;

import com.chatme.entity.Notification;
import com.chatme.repository.NotificationRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetNotificationsHandler implements QueryHandler<GetNotificationsHandler.Request, List<GetNotificationsHandler.NotificationDto>> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(String userId) {}

    @Builder
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NotificationDto {
        private String id;
        private String type;
        private String message;
        private boolean is_read;
        private Instant created_at;
    }

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationDto> handle(Request query) {
        List<Notification> notifications;
        if (query.userId() != null && !query.userId().isBlank()) {
            notifications = notificationRepository.findByUserId(query.userId());
        } else {
            notifications = notificationRepository.findAllDesc();
        }
        
        return notifications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private NotificationDto mapToDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .is_read(notification.getIsRead())
                .created_at(notification.getCreatedAt())
                .build();
    }
}
