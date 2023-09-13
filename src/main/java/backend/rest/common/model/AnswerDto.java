package backend.rest.common.model;

import backend.model.dao.AnswerDao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

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
    private String content;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date createdAt;

    @JsonProperty("top_answer")
    private boolean isTopAnswer;

    @JsonProperty("votes")
    private Integer votes;

    @JsonProperty("image")
    private String image;

    public static AnswerDto buildFromModel(AnswerDao answer) {
        return AnswerDto.builder()
                .answerId(answer.getId())
                .author(UserDto.buildFromModel(answer.getUser()))
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .isTopAnswer(answer.getIsTopAnswer())
                .votes(answer.getVotes().size())
                .build();
    }
}
