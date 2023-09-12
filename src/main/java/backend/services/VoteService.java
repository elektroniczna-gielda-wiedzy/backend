package backend.services;

import backend.model.dto.FavoriteSetterDto;
import backend.model.dto.VoteDto;
import backend.rest.common.StandardBody;
import backend.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    public ResponseEntity<StandardBody> voteForEntry(Integer entryId, VoteDto voteDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardBody> setFavoriteStatus(Integer entryId, FavoriteSetterDto favoriteSetterDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardBody> voteForAnswer(Integer entryId, Integer answerId, VoteDto voteDto) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
