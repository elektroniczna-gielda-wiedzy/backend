package backend.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class ActivityInfo {

    @JsonProperty("no_announcements")
    private Integer noAnnouncements;

    @JsonProperty("no_posts")
    private Integer noPosts;

    @JsonProperty("no_notes")
    private Integer noNotes;

    @JsonProperty("announcements_votes")
    private VotesStatistics announcementsVotes;

    @JsonProperty("notes_votes")
    private VotesStatistics notesVotes;

    @JsonProperty("posts_votes")
    private VotesStatistics postsVotes;

    @JsonProperty("answers_votes")
    private VotesStatistics answersVotes;
}
