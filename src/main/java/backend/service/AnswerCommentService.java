package backend.service;

import backend.model.Comment;
import org.springframework.stereotype.Service;

@Service
public class AnswerCommentService {
    public Comment createComment(Integer entryId, Integer answerId, String content) {
        // TODO
        return null;
    }

    public Comment editComment(Integer commentId, String content) {
        // TODO
        return null;
    }

    public void deleteComment(Integer commentId) {
        // TODO
    }
}
