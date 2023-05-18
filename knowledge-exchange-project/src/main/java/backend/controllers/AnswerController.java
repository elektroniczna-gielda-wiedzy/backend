package backend.controllers;

import backend.model.StandardResponse;
import backend.model.dto.AnswerDto;
import backend.services.AnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entry")
public class AnswerController {

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    private final AnswerService answerService;

    @GetMapping("{entry_id}/answer")
    public ResponseEntity<StandardResponse> getAnswerList(
            @PathVariable("entry_id") Integer entryId
            ) {
        return answerService.getAnswerList(entryId);
    }

    @PostMapping("{entry_id}/answer")
    public ResponseEntity<StandardResponse> addNewAnswer(
            @PathVariable("entry_id") Integer entryId,
            @RequestBody AnswerDto answerDto
            ) {
        return answerService.addNewAnswer(
                entryId,
                answerDto
        );
    }

    @PutMapping("{entry_id}/answer/{answer_id}")
    public ResponseEntity<StandardResponse> editAnswer(
            @PathVariable("entry_id") Integer entryId,
            @PathVariable("answer_id") Integer answerId,
            @RequestBody AnswerDto answerDto
    ) {
        return answerService.editAnswer(
                entryId,
                answerId,
                answerDto);
    }

    @DeleteMapping("{entry_id}/answer/{answer_id}")
    public ResponseEntity<StandardResponse> deleteAnswer(
            @PathVariable("entry_id") Integer entryId,
            @PathVariable("answer_id") Integer answerId
    ) {
        return answerService.deleteAnswer(
                entryId,
                answerId
        );
    }
}
