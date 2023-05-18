package backend.services;

import backend.model.StandardResponse;
import backend.model.dto.FavoriteSetterDto;
import backend.model.dto.VoteDto;
import backend.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    public ResponseEntity<StandardResponse> voteForEntry(
            Integer entryId,
            VoteDto voteDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> setFavoriteStatus(
            Integer entryId,
            FavoriteSetterDto favoriteSetterDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> voteForAnswer(
            Integer entryId,
            Integer answerId,
            VoteDto voteDto) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
