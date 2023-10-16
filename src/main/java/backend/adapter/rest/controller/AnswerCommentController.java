package backend.adapter.rest.controller;

import backend.adapter.rest.model.answer.CommentDto;
import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.answer.model.Comment;
import backend.answer.service.AnswerCommentService;
import backend.common.service.GenericServiceException;
import backend.user.model.AppUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entry")
public class AnswerCommentController {
    private final AnswerCommentService answerCommentService;

    public AnswerCommentController(AnswerCommentService answerCommentService) {
        this.answerCommentService = answerCommentService;
    }

    @GetMapping(path = "/{entry_id}/answer/{answer_id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getComments(@PathVariable("entry_id") Integer entryId,
                                                    @PathVariable("answer_id") Integer answerId) {
        List<Comment> comments;

        try {
            comments = this.answerCommentService.getComments(answerId);
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(comments.stream().map(CommentDto::buildFromModel).toList())
                .build();
    }


    @PostMapping(path = "/{entry_id}/answer/{answer_id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> createComment(@PathVariable("entry_id") Integer entryId,
                                                      @PathVariable("answer_id") Integer answerId,
                                                      @Valid @RequestBody CommentDto commentDto,
                                                      @AuthenticationPrincipal AppUserDetails userDetails) {
        Comment comment;

        try {
            comment = this.answerCommentService.createComment(
                    answerId,
                    userDetails.getId(),
                    commentDto.getContent()
            );
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(CommentDto.buildFromModel(comment)))
                .build();
    }

    @PutMapping(path = "/{entry_id}/answer/{answer_id}/comment/{comment_id}", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> editComment(@PathVariable("entry_id") Integer entryId,
                                                    @PathVariable("answer_id") Integer answerId,
                                                    @PathVariable("comment_id") Integer commentId,
                                                    @RequestBody CommentDto commentDto,
                                                    @AuthenticationPrincipal AppUserDetails userDetails) {
        Comment comment;

        try {
            comment = this.answerCommentService.editComment(
                    commentId,
                    userDetails.getId(),
                    commentDto.getContent()
            );
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(CommentDto.buildFromModel(comment)))
                .build();
    }

    @DeleteMapping(path = "/{entry_id}/answer/{answer_id}/comment/{comment_id}", consumes = MediaType.APPLICATION_JSON_VALUE,
                   produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> deleteComment(@PathVariable("entry_id") Integer entryId,
                                                      @PathVariable("answer_id") Integer answerId,
                                                      @PathVariable("comment_id") Integer commentId,
                                                      @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            this.answerCommentService.deleteComment(commentId, userDetails.getId());
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .build();
    }
}
