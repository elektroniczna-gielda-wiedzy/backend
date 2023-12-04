package backend;

import backend.answer.model.Answer;
import backend.answer.repository.AnswerRepository;
import backend.answer.service.AnswerCommentService;
import backend.answer.service.AnswerService;
import backend.common.service.EmailService;
import backend.entry.model.Entry;
import backend.entry.model.EntryType;
import backend.entry.repository.EntryRepository;
import backend.entry.service.EntryService;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpringContextRequiringTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerCommentService service;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private EntryService entryService;


    @MockBean
    EmailService emailService;

    protected Entry createMockEntry() {
        return entryService.createEntry(EntryType.POST.getId(),
                                 "title",
                                 "EntryContent", List.of(1, 13),
                                 1,
                                 null,
                                 null
                                 );
    }

    protected User getAdminUser() {
        return userRepository.findUserByEmail("admin@student.agh.edu.pl")
                .get();
    }

    protected User getSimpleUser() {
        return userRepository.findUserByEmail("akowalski@student.agh.edu.pl").get();
    }

    protected Answer createMockAnswer(Entry parentEntry) {
        return answerService.createAnswer(parentEntry.getId(),
                                   2,
                                   "content",
                                   null,
                                   null);

    }

    protected void deleteMockEntry(Entry entry) {
        try {
            this.entryService.deleteEntry(entry.getId(), 1);
        } catch (Exception e) {
            System.out.println("Entry is deleted");
        }

    }

    protected void deleteMockAnswer(Answer answer) {
        try {
            this.answerService.deleteAnswer(answer.getId(), 2);
        } catch (Exception e) {
            System.out.println("Answer is deleted");
        }
    }

    protected AutoDeleteTempEntry createTempMockEntry() {
        Entry entry = createMockEntry();
        return new AutoDeleteTempEntry(entry, entryRepository);
    }

    protected AutoDeleteTempAnswer createTempMockAnswer(Entry parentEntry) {
        Answer answer = createMockAnswer(parentEntry);
        return new AutoDeleteTempAnswer(answer, answerRepository);
    }
}