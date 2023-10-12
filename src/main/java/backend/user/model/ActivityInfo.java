package backend.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Getter
@Setter
@Builder
@Jacksonized
public class ActivityInfo {

    @JsonProperty("no_entries")
    private Map<String, Long> noEntries;

    @JsonProperty("no_votes")
    private Map<String, VotesStatistics> noVotes;
}
