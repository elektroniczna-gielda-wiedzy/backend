package backend.services;

import backend.model.StandardResponse;
import backend.model.dto.CommentDto;
import backend.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnswerCommentService {
    public ResponseEntity<StandardResponse> addCommentToAnswer(Integer entryId,
                                                               Integer answerId,
                                                               CommentDto commentDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> editComment(Integer entryId,
                                                        Integer answerId,
                                                        Integer commentId,
                                                        CommentDto commentDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> deleteComment(Integer entryId, Integer answerId, Integer commentId) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
