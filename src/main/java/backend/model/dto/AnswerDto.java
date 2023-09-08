package backend.model.dto;

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
    private Date createdAt;

    @JsonProperty("top_answer")
    private boolean isTopAnswer;

    @JsonProperty("votes")
    private Integer votes;

    @JsonProperty("image")
    private String image;
}
