package backend.rest.answer;

import backend.rest.common.Response;
import backend.rest.common.StandardBody;
import backend.rest.common.model.CommentDto;
import backend.services.AnswerCommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entry")
public class AnswerCommentController {
    private final AnswerCommentService answerCommentService;

    public AnswerCommentController(AnswerCommentService answerCommentService) {
        this.answerCommentService = answerCommentService;
    }

    @PostMapping("/{entry_id}/answer/{answer_id}/comment")
    public ResponseEntity<StandardBody> addCommentToAnswer(@PathVariable("entry_id") Integer entryId,
                                                           @PathVariable("answer_id") Integer answerId,
                                                           @RequestBody CommentDto commentDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PutMapping("/{entry_id}/answer/{answer_id}/comment/{comment_id}")
    public ResponseEntity<StandardBody> editComment(@PathVariable("entry_id") Integer entryId,
                                                    @PathVariable("answer_id") Integer answerId,
                                                    @PathVariable("comment_id") Integer commentId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @DeleteMapping("/{entry_id}/answer/{answer_id}/comment/{comment_id}")
    public ResponseEntity<StandardBody> deleteComment(@PathVariable("entry_id") Integer entryId,
                                                      @PathVariable("answer_id") Integer answerId,
                                                      @PathVariable("comment_id") Integer commentId) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }
}
