package backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteSetterDto {
    @JsonProperty("value")
    public Integer value;
}
