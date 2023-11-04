package backend.adapter.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class StandardBody {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("messages")
    private List<String> messages;

    @JsonProperty("result")
    private List<?> result;

    @JsonProperty("result_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResultInfo resultInfo;
}
