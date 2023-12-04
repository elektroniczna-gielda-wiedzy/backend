package backend.report.service;

import backend.SpringContextRequiringTestBase;
import backend.answer.model.Answer;
import backend.entry.model.Entry;
import backend.report.model.Report;
import backend.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReportServiceIntegrationTest extends SpringContextRequiringTestBase {

    @Autowired
    private ReportService reportService;

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
    @Order(1)
    public void testReportEntry() {
        User adminUser = getAdminUser();
        String title = "Bot entry";
        String content = "content";
        reportService.createReport(entry.getId(), 1, title, content);
        List<Report> reports = reportService.getReports(adminUser.getId());

        Assertions.assertThat(reports).hasSize(1);
        Assertions.assertThat(reports.get(0).getReporter().getId()).isEqualTo(1);
        Assertions.assertThat(reports.get(0).getEntry().getId()).isEqualTo(entry.getId());
        Assertions.assertThat(reports.get(0).getTopic()).isEqualTo(title);
        Assertions.assertThat(reports.get(0).getDescription()).isEqualTo(content);
    }

    @Test
    @Order(2)
    public void testResolveEntry() {
        User adminUser = getAdminUser();
        Report report = reportService.getReports(adminUser.getId()).get(0);
        reportService.updateReport(adminUser.getId(), report.getReportId(), 1);
        List<Report> reports = reportService.getReports(adminUser.getId());
        Assertions.assertThat(reports).isEmpty();
    }


}
