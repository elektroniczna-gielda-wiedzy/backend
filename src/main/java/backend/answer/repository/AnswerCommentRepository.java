package backend.answer.repository;

import backend.answer.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerCommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
}
