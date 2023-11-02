package backend.adapter.rest.model.report;

import backend.report.model.Report;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportResponseDto {
    @JsonProperty("report_id")
    private Integer reportId;

    @JsonProperty("topic")
    @NotNull(message = "topic cannot be null")
    private String topic;

    @JsonProperty("reporter_id")
    @NotNull(message = "reporter id cannot be null")
    private Integer reporterId;

    @JsonProperty("entry_id")
    @NotNull(message = "entry id cannot be null")
    private Integer entryId;

    @JsonProperty("description")
    @NotNull(message = "description cannot be null")
    private String description;

    public static ReportResponseDto buildFromModel(Report report, boolean details) {
        ReportResponseDto.ReportResponseDtoBuilder builder = ReportResponseDto.builder()
                .reportId(report.getReportId())
                .topic(report.getTopic())
                .reporterId(report.getReporter().getId())
                .entryId(report.getEntry().getId());

        if (details) {
            builder.description(report.getDescription());
        }
        return builder.build();
    }
}
