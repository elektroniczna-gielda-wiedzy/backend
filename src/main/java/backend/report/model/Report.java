package backend.report.model;

import backend.answer.model.Answer;
import backend.entry.model.Entry;
import backend.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;

@Entity
@Table(name="report")
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private int reportId;

    @JoinColumn(name = "entry_id")
    @ManyToOne
    private Entry entry;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Column(name = "topic")
    private String topic;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "reviewed")
    private Boolean reviewed;

    public static Specification<Report> isNotReviewed() {
        return (report, cq, cb) -> cb.isFalse(report.get("reviewed"));
    }
}
