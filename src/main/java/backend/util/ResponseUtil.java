package backend.util;

import backend.model.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {
    public static ResponseEntity<StandardResponse> getNotImplementedResponse() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(StandardResponse.builder()
                              .success(false)
                              .messages(List.of(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase()))
                              .result(List.of())
                              .build());
    }
}
