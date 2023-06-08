package backend.controllers;

import backend.model.StandardResponse;
import backend.model.dto.CommentDto;
import backend.services.AnswerCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/entry")
public class AnswerCommentController {

    public AnswerCommentController(
            AnswerCommentService answerCommentService
    ) {
        this.answerCommentService = answerCommentService;
    }

    private final AnswerCommentService answerCommentService;

    @PostMapping("/{entry_id}/answer/{answer_id}/comment")
    public ResponseEntity<StandardResponse> addCommentToAnswer(
            @PathVariable("entry_id") Integer entryId,
            @PathVariable("answer_id") Integer answerId,
            @RequestBody CommentDto commentDto
            ) {
        return answerCommentService.addCommentToAnswer(
                entryId,
                answerId,
                commentDto
        );
    }

    @PutMapping("/{entry_id}/answer/{answer_id}/comment/{comment_id}")
    public ResponseEntity<StandardResponse> editComment(
            @PathVariable("entry_id") Integer entryId,
            @PathVariable("answer_id") Integer answerId,
            @PathVariable("comment_id") Integer commentId,
            @RequestBody CommentDto commentDto
    ) {
        return answerCommentService.editComment(
                entryId,
                answerId,
                commentId,
                commentDto
        );
    }

    @DeleteMapping("/{entry_id}/answer/{answer_id}/comment/{comment_id}")
    public ResponseEntity<StandardResponse> deleteComment(
            @PathVariable("entry_id") Integer entryId,
            @PathVariable("answer_id") Integer answerId,
            @PathVariable("comment_id") Integer commentId
    ) {
        return answerCommentService.deleteComment(
                entryId,
                answerId,
                commentId
        );
    }
}
