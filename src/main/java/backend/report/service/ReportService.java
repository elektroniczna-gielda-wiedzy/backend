package backend.report.service;

import backend.common.service.GenericServiceException;
import backend.entry.model.Entry;
import backend.entry.repository.EntryRepository;
import backend.report.model.Report;
import backend.report.repository.ReportRepository;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final EntryRepository entryRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository, EntryRepository entryRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.entryRepository = entryRepository;
    }

    public Report createReport(Integer entryId, Integer reporterId, String topic, String description) {
        User user = this.userRepository.findById(reporterId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", reporterId)));

        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        Report report = new Report();

        report.setDescription(description);
        report.setTopic(topic);
        report.setEntry(entry);
        report.setReporter(user);
        report.setCreatedAt(Timestamp.from(Instant.now()));
        report.setReviewed(false);

        try {
            this.reportRepository.save(report);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return report;
    }

    public Report getReport(Integer userId, Integer reportId) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId))
        );

        if (!user.getIsAdmin()) {
            throw new GenericServiceException(String.format("User with id = %d is not an admin", userId));
        }

        try {
            return this.reportRepository.findById(reportId).orElseThrow(
                    () -> new GenericServiceException(String.format("Report with id = %d does not exist", reportId)));
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }

    public List<Report> getReports(Integer userId){
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId))
        );

        if (!user.getIsAdmin()) {
            throw new GenericServiceException(String.format("User with id = %d is not an admin", userId));
        }

        try {
            return this.reportRepository.findAll(Specification.where(Report.isNotReviewed()));
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }

    public Report updateReport(Integer userId, Integer reportId, Integer reviewed) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId))
        );

        if (!user.getIsAdmin()) {
            throw new GenericServiceException(String.format("User with id = %d is not an admin", userId));
        }

        Report report = this.reportRepository.findById(reportId).orElseThrow(
                () -> new GenericServiceException(String.format("Report with id = %d does not exist", reportId)));

        if (reviewed != null) {
            switch (reviewed) {
                case 1 -> report.setReviewed(true);
                case 0 -> report.setReviewed(false);
                default -> throw new GenericServiceException("Invalid review value");
            }
        }
        try {
            this.reportRepository.save(report);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return report;
    }
}
