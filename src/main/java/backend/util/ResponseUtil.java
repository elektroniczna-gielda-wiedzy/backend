package backend.util;

import backend.rest.common.StandardBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {
    public static ResponseEntity<StandardBody> getNotImplementedResponse() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(StandardBody.builder()
                              .success(false)
                              .messages(List.of(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase()))
                              .result(List.of())
                              .build());
    }
}
