package backend.controllers;

import backend.model.StandardResponse;
import backend.model.dto.FavoriteSetterDto;
import backend.model.dto.VoteDto;
import backend.services.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entry")
public class VoteController {

    public VoteController(
            VoteService voteService
    ) {
        this.voteService = voteService;
    }

    private VoteService voteService;

    @PutMapping("{entry_id}/vote")
    public ResponseEntity<StandardResponse> voteForEntry(
            @PathVariable("entry_id") Integer entryId,
            @RequestBody VoteDto voteDto) {
        return voteService.voteForEntry(
                entryId,
                voteDto
        );
    }

    @PutMapping("{entry_id}/favorite")
    public ResponseEntity<StandardResponse> setFavoriteStatus(
            @PathVariable("entry_id") Integer entryId,
            @RequestBody FavoriteSetterDto favoriteSetterDto
            ) {
        return voteService.setFavoriteStatus(
                entryId,
                favoriteSetterDto
        );
    }

    @PutMapping("{entry_id}/answer/{answer_id}/vote")
    public ResponseEntity<StandardResponse> voteForAnswer(
            @PathVariable("entry_id") Integer entryId,
            @PathVariable("answer_id") Integer answerId,
            @RequestBody VoteDto voteDto
    ) {
        return voteService.voteForAnswer(
                entryId,
                answerId,
                voteDto
        );
    }
}
