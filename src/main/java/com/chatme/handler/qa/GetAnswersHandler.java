package com.chatme.handler.qa;

import com.chatme.dto.qa.*;
import com.chatme.entity.Answer;
import com.chatme.entity.AnswerReaction;
import com.chatme.repository.AnswerRepository;
import com.chatme.repository.AnswerReactionRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GetAnswersHandler implements QueryHandler<GetAnswersQuery, AnswersResponse> {

    private final AnswerRepository answerRepository;
    private final AnswerReactionRepository reactionRepository;

    public GetAnswersHandler(AnswerRepository answerRepository, AnswerReactionRepository reactionRepository) {
        this.answerRepository = answerRepository;
        this.reactionRepository = reactionRepository;
    }

    @Override
    public AnswersResponse handle(GetAnswersQuery query) {
        int page = query.page() < 1 ? 1 : query.page();
        int limit = query.limit() < 1 ? 10 : query.limit();
        int offset = (page - 1) * limit;

        List<Answer> answers = answerRepository.findByQuestionId(query.questionId(), limit, offset);
        long total = answerRepository.countByQuestionId(query.questionId());

        List<AnswerDto> dtos = answers.stream()
            .map(this::mapAnswer)
            .collect(Collectors.toList());

        return AnswersResponse.builder()
            .data(dtos)
            .meta(AnswersResponse.Meta.builder()
                .total(total)
                .page(page)
                .limit(limit)
                .total_pages((int) Math.ceil((double) total / limit))
                .build())
            .build();
    }

    private AnswerDto mapAnswer(Answer a) {
        return AnswerDto.builder()
            .id(a.getId())
            .content(a.getContent())
            .author(UserDto.builder()
                .id(a.getAuthorId())
                .name(a.getAuthorName())
                .avatar_url(a.getAuthorAvatar())
                .build())
            .created_at(a.getCreatedAt())
            .reactions(getReactions(a.getId()))
            .build();
    }

    private List<ReactionDto> getReactions(String answerId) {
        List<AnswerReaction> reactions = reactionRepository.findByAnswerId(answerId);
        
        Map<String, Long> counts = reactions.stream()
            .collect(Collectors.groupingBy(AnswerReaction::getType, Collectors.counting()));

        return counts.entrySet().stream()
            .map(entry -> ReactionDto.builder()
                .type(entry.getKey())
                .count(entry.getValue().intValue())
                .user_reacted(checkUserReacted(reactions, entry.getKey()))
                .build())
            .collect(Collectors.toList());
    }
    
    private boolean checkUserReacted(List<AnswerReaction> reactions, String type) {
        String userId = "anon-user"; // Simplified
        return reactions.stream()
            .anyMatch(r -> r.getUserId().equals(userId) && r.getType().equals(type));
    }
}
