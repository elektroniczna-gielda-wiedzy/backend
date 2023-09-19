package backend.adapter.rest.controller;

import backend.adapter.rest.model.common.CommentDto;
import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.answer.service.AnswerCommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entry")
public class AnswerCommentController {
    private final AnswerCommentService answerCommentService;

    public AnswerCommentController(AnswerCommentService answerCommentService) {
        this.answerCommentService = answerCommentService;
    }

    @PostMapping(path = "/{entry_id}/answer/{answer_id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> addCommentToAnswer(@PathVariable("entry_id") Integer entryId,
                                                           @PathVariable("answer_id") Integer answerId,
                                                           @RequestBody CommentDto commentDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PutMapping(path = "/{entry_id}/answer/{answer_id}/comment/{comment_id}", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> editComment(@PathVariable("entry_id") Integer entryId,
                                                    @PathVariable("answer_id") Integer answerId,
                                                    @PathVariable("comment_id") Integer commentId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @DeleteMapping(path = "/{entry_id}/answer/{answer_id}/comment/{comment_id}", consumes = MediaType.APPLICATION_JSON_VALUE,
                   produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> deleteComment(@PathVariable("entry_id") Integer entryId,
                                                      @PathVariable("answer_id") Integer answerId,
                                                      @PathVariable("comment_id") Integer commentId) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }
}
