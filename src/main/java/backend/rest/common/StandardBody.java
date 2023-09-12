package backend.rest.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class StandardBody {
    private boolean success;

    private List<String> messages;

    private List<?> result;
}
