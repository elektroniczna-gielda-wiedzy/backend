package backend;

import backend.answer.model.Answer;
import backend.answer.repository.AnswerRepository;

public class AutoDeleteTempAnswer implements AutoCloseable {

    private final Answer answer;
    private final AnswerRepository answerRepository;
    public AutoDeleteTempAnswer(Answer answer, AnswerRepository answerRepository) {
        this.answer = answer;
        this.answerRepository = answerRepository;
    }

    @Override
    public void close() {
        this.answerRepository.delete(answer);
    }

    public Answer getAnswer() {
        return answer;
    }
}
