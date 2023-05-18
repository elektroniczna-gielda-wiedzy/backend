package backend.services;

import backend.model.StandardResponse;
import backend.model.dto.AnswerDto;
import backend.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    public ResponseEntity<StandardResponse> getAnswerList(
            Integer entryId
            ) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> addNewAnswer(
            Integer entryId,
            AnswerDto answerDto
            ) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> editAnswer(
            Integer entryId,
            Integer answerId,
            AnswerDto answerDto
    ) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> deleteAnswer(
            Integer entryId,
            Integer answerId
    ) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
