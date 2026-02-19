package com.chatme.handler.system;


import com.chatme.repository.BlogCommentRepository;
import com.chatme.repository.BlogPostRepository;
import com.chatme.repository.AnswerRepository;
import com.chatme.repository.QuestionRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetDashboardStatsHandler implements QueryHandler<GetDashboardStatsHandler.Query, GetDashboardStatsHandler.Response> {

    public record Query() {}

    public record Response(
        long total_posts,
        long total_comments,
        long total_questions,
        long total_answers,
        long total_users
    ) {
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private long total_posts;
            private long total_comments;
            private long total_questions;
            private long total_answers;
            private long total_users;
            public Builder total_posts(long v) { this.total_posts = v; return this; }
            public Builder total_comments(long v) { this.total_comments = v; return this; }
            public Builder total_questions(long v) { this.total_questions = v; return this; }
            public Builder total_answers(long v) { this.total_answers = v; return this; }
            public Builder total_users(long v) { this.total_users = v; return this; }
            public Response build() { return new Response(total_posts, total_comments, total_questions, total_answers, total_users); }
        }
    }

    private final BlogPostRepository blogPostRepository;
    private final BlogCommentRepository blogCommentRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public Response handle(Query query) {
        long totalPosts = blogPostRepository.count();
        long totalComments = blogCommentRepository.count();
        long totalQuestions = questionRepository.count();
        long totalAnswers = answerRepository.count();
        long totalUsers = 0; // User management not implemented yet

        return Response.builder()
                .total_posts(totalPosts)
                .total_comments(totalComments)
                .total_questions(totalQuestions)
                .total_answers(totalAnswers)
                .total_users(totalUsers)
                .build();
    }
}
