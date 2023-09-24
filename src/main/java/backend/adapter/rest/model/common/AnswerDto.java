package backend.adapter.rest.model.common;

import backend.answer.model.Answer;
import backend.answer.model.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@Jacksonized
public class AnswerDto {
    @JsonProperty("answer_id")
    private Integer answerId;

    @JsonProperty("author")
    private UserDto author;

    @JsonProperty("content")
    @NotNull(message = "content cannot be null")
    private String content;

    @JsonProperty("comments")
    private List<CommentDto> comments;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date createdAt;

    @JsonProperty("top_answer")
    private boolean isTopAnswer;

    @JsonProperty("votes")
    private Integer votes;

    @JsonProperty("image")
    private String image;

    public static AnswerDto buildFromModel(Answer answer) {
        return AnswerDto.builder()
                .answerId(answer.getId())
                .author(UserDto.buildFromModel(answer.getUser()))
                .content(answer.getContent())
                .comments(Optional.ofNullable(answer.getComments())
                                  .orElseGet(Collections::emptySet)
                                  .stream()
                                  .map(CommentDto::buildFromModel)
                                  .toList())
                .createdAt(answer.getCreatedAt())
                .isTopAnswer(answer.getIsTopAnswer())
                .votes(answer.getVotes().size())
                .build();
    }
}
