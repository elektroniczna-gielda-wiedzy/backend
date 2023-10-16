package backend.adapter.rest.controller;

import backend.adapter.rest.model.answer.AnswerRequestDto;
import backend.adapter.rest.model.answer.AnswerResponseDto;
import backend.user.model.AppUserDetails;
import backend.answer.model.Answer;
import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.answer.service.AnswerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entry")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping(path = "{entry_id}/answer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getAnswers(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                                   @PathVariable("entry_id") Integer entryId) {
        List<Answer> answers;

        try {
            answers = this.answerService.getAnswers(entryId);
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(answers.stream().
                        map((answer) -> AnswerResponseDto.buildFromModel(answer, appUserDetails.getId())).toList())
                .build();
    }

    @PostMapping(path ="{entry_id}/answer", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> createAnswer(@PathVariable("entry_id") Integer entryId,
                                                     @Valid @RequestBody AnswerRequestDto answerRequestDto,
                                                     @AuthenticationPrincipal AppUserDetails userDetails) {
        Answer answer;

        try {
            answer = this.answerService.createAnswer(
                    entryId,
                    userDetails.getId(),
                    answerRequestDto.getContent()
            );
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(AnswerResponseDto.buildFromModel(answer, userDetails.getId())))
                .build();
    }

    @PutMapping(path = "{entry_id}/answer/{answer_id}", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> editAnswer(@PathVariable("entry_id") Integer entryId,
                                                   @PathVariable("answer_id") Integer answerId,
                                                   @RequestBody AnswerRequestDto answerRequestDto,
                                                   @AuthenticationPrincipal AppUserDetails userDetails) {
        Answer answer;

        try {
            answer = this.answerService.editAnswer(
                    answerId,
                    userDetails.getId(),
                    answerRequestDto.getContent()
            );
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(AnswerResponseDto.buildFromModel(answer, userDetails.getId())))
                .build();
    }

    @DeleteMapping(path = "{entry_id}/answer/{answer_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> deleteAnswer(@PathVariable("entry_id") Integer entryId,
                                                     @PathVariable("answer_id") Integer answerId,
                                                     @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            this.answerService.deleteAnswer(answerId, userDetails.getId());
        } catch (Exception exception) {
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
