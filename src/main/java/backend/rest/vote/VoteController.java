package backend.rest.vote;

import backend.rest.common.Response;
import backend.rest.common.StandardBody;
import backend.rest.vote.model.FavoriteSetterDto;
import backend.rest.vote.model.VoteDto;
import backend.services.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entry")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PutMapping("{entry_id}/vote")
    public ResponseEntity<StandardBody> voteForEntry(@PathVariable("entry_id") Integer entryId,
                                                     @Valid @RequestBody
                                                     VoteDto voteDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PutMapping("{entry_id}/favorite")
    public ResponseEntity<StandardBody> setFavoriteStatus(@PathVariable("entry_id") Integer entryId,
                                                          @Valid @RequestBody FavoriteSetterDto favoriteSetterDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PutMapping("{entry_id}/answer/{answer_id}/vote")
    public ResponseEntity<StandardBody> voteForAnswer(@PathVariable("entry_id") Integer entryId,
                                                      @PathVariable("answer_id") Integer answerId,
                                                      @Valid @RequestBody VoteDto voteDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }
}
