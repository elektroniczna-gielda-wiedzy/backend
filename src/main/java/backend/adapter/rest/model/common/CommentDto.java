package backend.adapter.rest.model.common;

import backend.answer.model.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

@Getter
@Setter
@Builder
@Jacksonized
public class CommentDto {
    @JsonProperty("comment_id")
    private Integer commentId;

    @JsonProperty("author")
    private UserDto author;

    @JsonProperty("content")
    @NotNull(message = "content cannot be null")
    private String content;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date createdAt;

    public static CommentDto buildFromModel(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getId())
                .author(UserDto.buildFromModel(comment.getAuthor()))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
