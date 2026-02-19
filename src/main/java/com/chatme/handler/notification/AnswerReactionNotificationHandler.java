package com.chatme.handler.notification;

import com.chatme.event.AnswerReactedEvent;
import com.chatme.entity.AnswerReaction;
import com.chatme.entity.Notification;
import com.chatme.repository.AnswerReactionRepository;
import com.chatme.repository.NotificationRepository;
import com.fast.cqrs.cqrs.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AnswerReactionNotificationHandler implements EventHandler<AnswerReactedEvent> {

    private final NotificationRepository notificationRepository;
    private final AnswerReactionRepository answerReactionRepository;

    @Async
    @Override
    public void handle(AnswerReactedEvent event) {
        if (event.answerAuthorId() == null) return;
        if (event.userId().equals(event.answerAuthorId())) return;

        List<AnswerReaction> reactions = answerReactionRepository.findByAnswerId(event.answerId());
        String message = buildMessage(reactions.size(), "answer");

        createOrUpdateNotification(event.answerAuthorId(), "REACTION", message);
    }

    private String buildMessage(int count, String targetType) {
        if (count <= 1) {
            return "Someone reacted to your " + targetType;
        } else {
            return "Someone and " + (count - 1) + " others reacted to your " + targetType;
        }
    }

    private void createOrUpdateNotification(String userId, String type, String message) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .type(type)
                .message(message)
                .isRead(false)
                .createdAt(Instant.now())
                .build();
        
        notificationRepository.save(notification);
    }
}
