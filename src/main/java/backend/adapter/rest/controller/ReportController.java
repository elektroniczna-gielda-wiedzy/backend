package backend.adapter.rest.controller;

import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.adapter.rest.model.report.ReportRequestDto;
import backend.adapter.rest.model.report.ReportResponseDto;
import backend.common.service.GenericServiceException;
import backend.report.model.Report;
import backend.report.service.ReportService;
import backend.user.model.AppUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> createReport(@AuthenticationPrincipal AppUserDetails userDetails,
                                                     @Valid @RequestBody
                                                     ReportRequestDto reportRequestDto) {

        Report report;
        try {
            report = reportService.createReport(
                    reportRequestDto.getEntryId(),
                    userDetails.getId(),
                    reportRequestDto.getTopic(),
                    reportRequestDto.getDescription()
            );
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(ReportResponseDto.buildFromModel(report, true)))
                .build();
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getReports(@AuthenticationPrincipal AppUserDetails userDetails) {
        List<Report> reports;

        try {
            reports = this.reportService.getReports(userDetails.getId());
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(reports.stream()
                                .map(report -> ReportResponseDto.buildFromModel(report, false))
                                .toList())
                .build();
    }

    @GetMapping(path="/{report_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getReport(@AuthenticationPrincipal AppUserDetails userDetails,
            @PathVariable("report_id") Integer reportId) {
        Report report;

        try {
            report = reportService.getReport(userDetails.getId(), reportId);
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(ReportResponseDto.buildFromModel(report, true)))
                .build();

    }

    @PutMapping(path="/{report_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> updateReport(@AuthenticationPrincipal AppUserDetails userDetails,
                                                     @PathVariable("report_id") Integer reportId,
                                                     @RequestBody ReportRequestDto reportRequestDto) {
        Report report;

        try {
            report = reportService.updateReport(userDetails.getId(), reportId, reportRequestDto.getReviewed());
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(ReportResponseDto.buildFromModel(report, true)))
                .build();
    }
}
