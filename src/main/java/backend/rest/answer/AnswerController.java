package backend.rest.answer;

import backend.model.AppUserDetails;
import backend.model.dao.Answer;
import backend.rest.common.Response;
import backend.rest.common.StandardBody;
import backend.rest.common.model.AnswerDto;
import backend.services.AnswerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @GetMapping("{entry_id}/answer")
    public ResponseEntity<StandardBody> getAnswers(@PathVariable("entry_id") Integer entryId) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PostMapping("{entry_id}/answer")
    public ResponseEntity<StandardBody> addNewAnswer(
            @PathVariable("entry_id") Integer entryId,
            @Valid @RequestBody AnswerDto answerDto,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        Answer answer;

        try {
            answer = this.answerService.createAnswer(
                    entryId,
                    userDetails.getId(),
                    answerDto.getContent()
            );
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(AnswerDto.buildFromModel(answer)))
                .build();
    }

    @PutMapping("{entry_id}/answer/{answer_id}")
    public ResponseEntity<StandardBody> editAnswer(
            @PathVariable("entry_id") Integer entryId,
            @PathVariable("answer_id") Integer answerId,
            @RequestBody AnswerDto answerDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @DeleteMapping("{entry_id}/answer/{answer_id}")
    public ResponseEntity<StandardBody> deleteAnswer(
            @PathVariable("entry_id") Integer entryId,
            @PathVariable("answer_id") Integer answerId) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }
}
