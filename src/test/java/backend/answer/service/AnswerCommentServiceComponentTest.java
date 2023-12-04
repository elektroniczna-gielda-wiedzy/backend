package backend.answer.service;

import backend.SpringContextRequiringTestBase;
import backend.answer.model.Answer;
import backend.answer.model.Comment;
import backend.answer.repository.AnswerCommentRepository;
import backend.entry.model.Entry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AnswerCommentServiceComponentTest extends SpringContextRequiringTestBase {

    @Autowired
    private AnswerCommentRepository repository;

    @Autowired
    private AnswerCommentService service;

    private Answer answer;
    private Entry entry;
    private String commentContent = "NewComment";

    @BeforeAll
    public void setupTest() {
        entry = createMockEntry();
        answer = createMockAnswer(entry);
    }

    @AfterAll
    public void teardownTest() {
        deleteMockAnswer(answer);
        deleteMockEntry(entry);
    }

    @Test
    @Order(1)
    public void testCreateNewComment() {
        Integer answerId = answer.getId();
        Integer userId = 1;
        Comment comment = service.createComment(answerId, userId, commentContent);
        Optional<Comment> commentOptional = repository.findById(comment.getId());
        Comment commentRes = commentOptional.get();

        Assertions.assertThat(commentOptional).isNotEmpty();
        Assertions.assertThat(commentRes.getContent()).isEqualTo(commentContent);
        Assertions.assertThat(commentRes.getAnswer().getId()).isEqualTo(answerId);
        Assertions.assertThat(commentRes.getAuthor().getId()).isEqualTo(userId);
    }

    @Test
    @Order(2)
    public void testListComments() {
        List<Comment> comments = service.getComments(answer.getId());

        Assertions.assertThat(comments.size()).isEqualTo(1);
        Assertions.assertThat(comments.get(0).getContent()).isEqualTo(commentContent);
        Assertions.assertThat(comments.get(0).getAuthor().getId()).isEqualTo(1);
    }

    @Test
    @Order(3)
    public void testEditComment() {
        Comment comment = service.getComments(answer.getId()).get(0);
        service.editComment(comment.getId(), 1, "Content2");
        Comment editedComment = service.getComments(answer.getId()).get(0);

        Assertions.assertThat(editedComment.getContent()).isEqualTo("Content2");
    }

    @Test
    @Order(4)
    public void testDeleteComment() {
        Comment comment = service.getComments(answer.getId()).get(0);
        service.deleteComment(comment.getId(), 1);
        List<Comment> comments = service.getComments(answer.getId());

        Assertions.assertThat(comments.size()).isEqualTo(0);
    }
}
