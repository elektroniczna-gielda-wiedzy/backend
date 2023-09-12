package backend.services;

import backend.model.dto.CommentDto;
import backend.rest.common.StandardBody;
import backend.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnswerCommentService {
    public ResponseEntity<StandardBody> addCommentToAnswer(Integer entryId, Integer answerId, CommentDto commentDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardBody> editComment(Integer entryId,
                                                    Integer answerId,
                                                    Integer commentId,
                                                    CommentDto commentDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardBody> deleteComment(Integer entryId, Integer answerId, Integer commentId) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
