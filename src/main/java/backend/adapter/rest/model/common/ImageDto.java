package backend.adapter.rest.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {
    @JsonProperty("filename")
    @NotNull(message = "filename cannot be null")
    private String filename;

    @JsonProperty("data")
    @NotNull(message = "data cannot be null")
    private String data;
}
