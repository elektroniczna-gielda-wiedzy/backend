package backend.answer.service;

import backend.SpringContextRequiringTestBase;
import backend.answer.model.Answer;
import backend.entry.model.Entry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

public class AnswerServiceIntegrationTest extends SpringContextRequiringTestBase {

    @Autowired
    private AnswerService answerService;
    Entry entry;
    Answer answer;

    @BeforeEach
    public void setup() {
        entry = createMockEntry();
        answer = createMockAnswer(entry);
    }

    @AfterEach
    public void teardown() {
        deleteMockAnswer(answer);
        deleteMockEntry(entry);
    }

    @Test
    public void testCreateAnswer() {
        String content = "content";

        Answer answer = answerService.createAnswer(entry.getId(),
                                                   1,
                                                   content,
                                                   null,
                                                   null);

        Assertions.assertThat(answer.getContent()).isEqualTo(content);
    }

    @Test
    public void testEditAnswer() {
        String content = "editedContent";

        answerService.editAnswer(answer.getId(), answer.getUser().getId(), content, null, null);
        Answer editedAnswer = answerService.getAnswers(entry.getId()).get(0);
        Assertions.assertThat(editedAnswer.getContent()).isEqualTo(content);
    }

    @Test
    public void testDeleteAnswer() {
        answerService.deleteAnswer(answer.getId(), answer.getUser().getId());
        Assertions.assertThat(answerService.getAnswers(entry.getId())).hasSize(0);
    }
}
