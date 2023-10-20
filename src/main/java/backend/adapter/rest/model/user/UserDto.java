package backend.adapter.rest.model.user;

import backend.common.model.Vote;
import backend.entry.model.Entry;
import backend.entry.model.EntryType;
import backend.user.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Jacksonized
public class UserDto {
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("is_banned")
    private Boolean isBanned;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("is_email_auth")
    private Boolean isEmailAuth;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("email")
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("last_login")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date lastLogin;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("entries_count")
    private List<UserEntriesCountDto> entries_count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("votes_count")
    private List<UserVotesCountDto> votes_count;

    public static UserDto buildFromModel(User user, User requestedUser, boolean statistics) {
        UserDtoBuilder builder = UserDto.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName());

        if (statistics && (requestedUser.getIsAdmin() || user.getId().equals(requestedUser.getId()))) {
            builder.email(user.getEmail())
                    .isBanned(user.getIsBanned())
                    .isEmailAuth(user.getIsEmailAuth());

            Map<EntryType, Long> entriesCount = user.getEntries().stream()
                    .filter(entry -> !entry.getIsDeleted())
                    .map(Entry::getType)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            entriesCount.putIfAbsent(EntryType.NOTE, 0L);
            entriesCount.putIfAbsent(EntryType.ANNOUNCEMENT, 0L);
            entriesCount.putIfAbsent(EntryType.POST, 0L);

            Map<EntryType, UserVotesCountDto> votesCount = new HashMap<>();

            user.getEntries().stream()
                    .collect(Collectors.groupingBy(Entry::getType))
                    .forEach((entryType, entries) -> {
                         Map<Object, Long> votes = entries.stream()
                                 .flatMap((entry) -> entry.getVotes()
                                         .stream()
                                         .collect(Collectors.groupingBy(Vote::getValue, Collectors.counting()))
                                         .entrySet()
                                         .stream())
                                 .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));

                         votesCount.put(entryType, new UserVotesCountDto(entryType.getId(),
                                                                         entryType.getName(),
                                                                         votes.getOrDefault(1, 0L),
                                                                         votes.getOrDefault(-1, 0L)));
                    });

            votesCount.putIfAbsent(EntryType.NOTE, new UserVotesCountDto(EntryType.NOTE.getId(),
                                                                         EntryType.NOTE.getName(),
                                                                         0L,
                                                                         0L));

            votesCount.putIfAbsent(EntryType.ANNOUNCEMENT, new UserVotesCountDto(EntryType.ANNOUNCEMENT.getId(),
                                                                                 EntryType.ANNOUNCEMENT.getName(),
                                                                                 0L,
                                                                         0L));

            votesCount.putIfAbsent(EntryType.POST, new UserVotesCountDto(EntryType.POST.getId(),
                                                                         EntryType.POST.getName(),
                                                                         0L,
                                                                         0L));

            builder.createdAt(user.getCreatedAt())
                    .lastLogin(user.getLastLogin())
                    .entries_count(entriesCount.entrySet().stream()
                        .map(e -> new UserEntriesCountDto(e.getKey().getId(), e.getKey().getName(), e.getValue()))
                                           .sorted(Comparator.comparingInt(UserEntriesCountDto::getEntryTypeId))
                        .toList()
                    )
                    .votes_count(votesCount.values().stream().
                            sorted(Comparator.comparingInt(UserVotesCountDto::getEntryTypeId)).toList());
        }

        return builder.build();
    }
}
