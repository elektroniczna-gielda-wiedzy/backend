package backend.answer.service;

import backend.answer.model.Answer;
import backend.answer.model.Comment;
import backend.answer.repository.AnswerCommentRepository;
import backend.answer.repository.AnswerRepository;
import backend.common.model.ErrorMessageFormat;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class AnswerCommentServiceTest {
    @Test
    public void shouldThrowExceptionWhenAnswerDoesNotExist() {
        Integer nonExistingAnswerId = 2;
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        AnswerCommentService service = new AnswerCommentService(Mockito.mock(UserRepository.class),
                                                                answerRepository,
                                                                Mockito.mock(AnswerCommentRepository.class)
        );

        Mockito.when(answerRepository.findById(nonExistingAnswerId)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.getComments(nonExistingAnswerId)).withMessage(
                String.format(ErrorMessageFormat.ANSWER_NOT_FOUND, nonExistingAnswerId)
        );
    }

    @Test
    public void shouldThrowExceptionWhenUnknownUserCreatesComment() {
        Integer answerId = 2;
        Integer nonExistingUserId = 1;
        String commentContent = "Comment content";
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AnswerCommentService service = new AnswerCommentService(userRepository,
                                                                answerRepository,
                                                                Mockito.mock(AnswerCommentRepository.class)
        );

        Mockito.when(answerRepository.findById(answerId)).thenReturn(Optional.of(new Answer()));
        Mockito.when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.createComment(answerId,
                                                                                nonExistingUserId,
                                                                                commentContent
        )).withMessage(
                String.format(ErrorMessageFormat.USER_NOT_FOUND, nonExistingUserId)
        );
    }

    @Test
    public void shouldThrowExceptionWhenCreatingCommentForUnknownAnswer() {
        Integer nonExistingAnswerId = 2;
        Integer userId = 1;
        String commentContent = "Comment content";
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AnswerCommentService service = new AnswerCommentService(userRepository,
                                                                answerRepository,
                                                                Mockito.mock(AnswerCommentRepository.class)
        );

        Mockito.when(answerRepository.findById(nonExistingAnswerId)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Assertions.assertThatException().isThrownBy(() -> service.createComment(nonExistingAnswerId,
                                                                                userId,
                                                                                commentContent
                                                                                )).withMessage(
                String.format(ErrorMessageFormat.ANSWER_NOT_FOUND, nonExistingAnswerId)
        );
    }

    @Test
    public void shouldThrowExceptionWhenUnknownUserEditsComment() {
        Integer commentId = 2;
        Integer nonExistingUserId = 1;
        String newCommentContent = "Comment content";
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AnswerCommentRepository commentRepository = Mockito.mock(AnswerCommentRepository.class);
        AnswerCommentService service = new AnswerCommentService(userRepository,
                                                                answerRepository,
                                                                commentRepository);

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));
        Mockito.when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.editComment(commentId,
                                                                                nonExistingUserId,
                                                                                newCommentContent
        )).withMessage(
                String.format(ErrorMessageFormat.USER_NOT_FOUND, nonExistingUserId)
        );
    }

    @Test
    public void shouldThrowExceptionWhenEditingUnknownComment() {
        Integer nonExistingCommentId = 2;
        Integer userId = 1;
        String newCommentContent = "New comment content";
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AnswerCommentRepository commentRepository = Mockito.mock(AnswerCommentRepository.class);
        AnswerCommentService service = new AnswerCommentService(userRepository,
                                                                answerRepository,
                                                                commentRepository);

        Mockito.when(commentRepository.findById(nonExistingCommentId)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Assertions.assertThatException().isThrownBy(() -> service.editComment(nonExistingCommentId,
                                                                              userId,
                                                                              newCommentContent
        )).withMessage(
                String.format(ErrorMessageFormat.COMMENT_NOT_FOUND, nonExistingCommentId)
        );
    }

    @Test
    public void shouldThrowExceptionWhenDeletingUnknownComment() {
        Integer nonExistingCommentId = 2;
        Integer userId = 1;
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AnswerCommentRepository commentRepository = Mockito.mock(AnswerCommentRepository.class);
        AnswerCommentService service = new AnswerCommentService(userRepository,
                                                                answerRepository,
                                                                commentRepository);

        Mockito.when(commentRepository.findById(nonExistingCommentId)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Assertions.assertThatException().isThrownBy(() -> service.deleteComment(nonExistingCommentId,
                                                                              userId
        )).withMessage(
                String.format(ErrorMessageFormat.COMMENT_NOT_FOUND, nonExistingCommentId)
        );
    }
}
