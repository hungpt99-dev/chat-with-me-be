package com.chatme.handler.notification;

import com.chatme.event.QuestionReactedEvent;
import com.chatme.entity.Notification;
import com.chatme.entity.QuestionReaction;
import com.chatme.repository.NotificationRepository;
import com.chatme.repository.QuestionReactionRepository;
import com.fast.cqrs.cqrs.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuestionReactionNotificationHandler implements EventHandler<QuestionReactedEvent> {

    private final NotificationRepository notificationRepository;
    private final QuestionReactionRepository questionReactionRepository;

    @Async
    @Override
    public void handle(QuestionReactedEvent event) {
        if (event.questionAuthorId() == null) return; // Cannot notify
        if (event.userId().equals(event.questionAuthorId())) return; // Don't notify self

        List<QuestionReaction> reactions = questionReactionRepository.findByQuestionId(event.questionId());
        String message = buildMessage(reactions.size(), "question");

        createOrUpdateNotification(event.questionAuthorId(), "REACTION", message);
    }

    private String buildMessage(int count, String targetType) {
        if (count <= 1) {
            return "Someone reacted to your " + targetType;
        } else {
            return "Someone and " + (count - 1) + " others reacted to your " + targetType;
        }
    }

    private void createOrUpdateNotification(String userId, String type, String message) {
        // Simplified: generally we might want to update an existing unread notification 
        // to avoid spamming, but for now we just create a new one as per original logic's simplicity 
        // (Original logic was named createOrUpdate but implementation was just save new one, 
        // assuming standard append-only log or similar. We keep it simple).
        
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
