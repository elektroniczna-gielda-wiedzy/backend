package backend.rest.controller;

import backend.rest.Response;
import backend.rest.StandardBody;
import backend.rest.model.vote.FavoriteSetterDto;
import backend.rest.model.vote.VoteDto;
import backend.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entry")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PutMapping(path = "{entry_id}/vote", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> voteForEntry(@PathVariable("entry_id") Integer entryId,
                                                     @Valid @RequestBody
                                                     VoteDto voteDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PutMapping(path = "{entry_id}/favorite", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> setFavoriteStatus(@PathVariable("entry_id") Integer entryId,
                                                          @Valid @RequestBody FavoriteSetterDto favoriteSetterDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PutMapping(path =  "{entry_id}/answer/{answer_id}/vote", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> voteForAnswer(@PathVariable("entry_id") Integer entryId,
                                                      @PathVariable("answer_id") Integer answerId,
                                                      @Valid @RequestBody VoteDto voteDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }
}
