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
public class VotesStatistics {
    @JsonProperty("positive")
    private Long positive;

    @JsonProperty("negative")
    private Long negative;
}
