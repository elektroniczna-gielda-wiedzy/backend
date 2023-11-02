package backend.adapter.rest.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReportRequestDto {
    @JsonProperty("topic")
    @NotNull(message = "topic cannot be null")
    private String topic;

    @JsonProperty("entry_id")
    @NotNull(message = "entry id cannot be null")
    private Integer entryId;

    @JsonProperty("description")
    @NotNull(message = "description cannot be null")
    private String description;

    @JsonProperty("reviewed")
    private Integer reviewed;
}
