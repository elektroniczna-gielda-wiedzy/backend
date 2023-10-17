package backend.adapter.rest.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Setter
@Getter
@Jacksonized
public class UserVotesCountDto {
    @JsonProperty("entry_type_id")
    private Integer entryTypeId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("positive")
    private Long positive;

    @JsonProperty("negative")
    private Long negative;
}
