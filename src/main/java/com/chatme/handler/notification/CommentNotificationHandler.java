package com.chatme.handler.notification;

import com.chatme.event.CommentAddedEvent;
import com.chatme.entity.Notification;
import com.chatme.repository.NotificationRepository;
import com.fast.cqrs.cqrs.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentNotificationHandler implements EventHandler<CommentAddedEvent> {

    private final NotificationRepository notificationRepository;

    @Async
    @Override
    public void handle(CommentAddedEvent event) {
        if (event.postAuthorId() == null) return;
        
        String message = "New comment by " + event.authorName() + " on post " + event.postId(); // ideally post title if we had it
        createOrUpdateNotification(event.postAuthorId(), "COMMENT", message);
    }

    private void createOrUpdateNotification(String userId, String type, String message) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .type(type)
                .message(message)
                .isRead(false)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        
        notificationRepository.save(notification);
    }
}
