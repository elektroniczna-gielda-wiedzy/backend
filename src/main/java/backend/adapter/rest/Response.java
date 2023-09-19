package backend.adapter.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class Response {
    private final List<String> messages = new ArrayList<>();

    private HttpStatusCode httpStatusCode = HttpStatus.OK;

    private List<?> result = new ArrayList<>();

    public static Response builder() {
        return new Response();
    }

    public Response httpStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        return this;
    }

    public Response addMessage(String message) {
        this.messages.add(message);
        return this;
    }

    public Response result(List<?> result) {
        this.result = result;
        return this;
    }

    public ResponseEntity<StandardBody> build() {
        StandardBody standardBody = StandardBody.builder()
                .success(httpStatusCode.is2xxSuccessful())
                .messages(messages)
                .result(result)
                .build();
        return new ResponseEntity<>(standardBody, httpStatusCode);
    }
}
