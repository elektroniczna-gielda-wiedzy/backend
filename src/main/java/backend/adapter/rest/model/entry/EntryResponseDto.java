package backend.adapter.rest.model.entry;

import backend.adapter.rest.model.answer.AnswerResponseDto;
import backend.adapter.rest.model.common.CategoryDto;
import backend.adapter.rest.model.user.UserDto;
import backend.answer.model.Answer;
import backend.common.model.Vote;
import backend.entry.model.Entry;
import backend.user.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.*;

@Getter
@Setter
@Builder
@Jacksonized
@AllArgsConstructor
public class EntryResponseDto {
    @JsonProperty("entry_id")
    private Integer entryId;

    @JsonProperty("entry_type_id")
    @NotNull(message = "entry_type_id cannot be null")
    private Integer entryTypeId;

    @JsonProperty("title")
    @NotNull(message = "title cannot be null")
    private String title;

    @JsonProperty("author")
    private UserDto author;

    @JsonProperty("user_vote")
    private Integer voteValue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("content")
    @NotNull(message = "content cannot be null")
    private String content;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("image")
    private String image;

    @JsonProperty("categories")
    @NotNull(message = "categories cannot be null")
    private List<CategoryDto> categories;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("favorite")
    private boolean favorite;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("answers")
    private List<AnswerResponseDto> answers;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("votes")
    private Integer votes;

    public static EntryResponseDto buildFromModel(Entry entry, User user, boolean content, List<Answer> answers) {
        EntryResponseDtoBuilder builder = EntryResponseDto.builder()
                .entryId(entry.getId())
                .entryTypeId(entry.getType().getId())
                .title(entry.getTitle())
                .author(UserDto.buildFromModel(entry.getAuthor(), user, false))
                .createdAt(entry.getCreatedAt())
                .categories(entry.getCategories().stream().map(CategoryDto::buildFromModel).toList())
                .votes(entry.getVotes().stream().mapToInt(Vote::getValue).sum())
                .voteValue(entry.getVotes().stream()
                                   .filter(vote -> vote.getUser().getId().equals(user.getId()))
                                   .mapToInt(Vote::getValue).sum())
                .favorite(entry.getLikedBy().stream().anyMatch(u -> u.getId().equals(user.getId())))
                .image(entry.getImage() != null ? "/image/" + entry.getImage() : null); // TODO: Generate valid URL.


        if (content) {
            builder.content(entry.getContent());
        }

        if (answers != null) {
            builder.answers(answers.stream()
                                    .map(answer -> AnswerResponseDto.buildFromModel(answer, user.getId()))
                                    .toList());
        }

        return builder.build();
    }
}
