package com.chatme.handler.notification;

import com.chatme.entity.Notification;
import com.chatme.repository.NotificationRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarkNotificationReadHandler implements CommandHandler<MarkNotificationReadHandler.Request> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(String id) {}

    private final NotificationRepository notificationRepository;

    @Override
    public void handle(Request cmd) {
        notificationRepository.findById(cmd.id()).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }
}
