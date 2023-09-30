package backend.adapter.rest.controller;

import backend.adapter.rest.model.vote.FavoriteDto;
import backend.adapter.rest.model.vote.VoteDto;
import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.common.service.GenericServiceException;
import backend.common.service.VoteService;
import backend.user.model.AppUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                                                     @Valid @RequestBody VoteDto voteDto,
                                                     @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            this.voteService.voteForEntry(entryId, userDetails.getId(), voteDto.getValue());
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

    @PutMapping(path = "{entry_id}/favorite", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> setFavoriteStatus(@PathVariable("entry_id") Integer entryId,
                                                          @Valid @RequestBody FavoriteDto favoriteDto,
                                                          @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            this.voteService.setFavoriteStatus(entryId, userDetails.getId(), favoriteDto.getValue());
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

    @PutMapping(path =  "{entry_id}/answer/{answer_id}/vote", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> voteForAnswer(@PathVariable("entry_id") Integer entryId,
                                                      @PathVariable("answer_id") Integer answerId,
                                                      @Valid @RequestBody VoteDto voteDto,
                                                      @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            this.voteService.voteForAnswer(answerId, userDetails.getId(), voteDto.getValue());
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
