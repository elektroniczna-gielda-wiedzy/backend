package backend.adapter.rest.controller;

import backend.adapter.rest.model.common.CategoryDto;
import backend.adapter.rest.model.entry.EntryRequestDto;
import backend.adapter.rest.model.entry.EntryResponseDto;
import backend.answer.model.Answer;
import backend.answer.service.AnswerService;
import backend.entry.model.EntrySortMode;
import backend.user.model.AppUserDetails;
import backend.entry.model.Entry;
import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.entry.service.EntryService;
import backend.common.service.GenericServiceException;
import jakarta.validation.Valid;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/entry")
public class EntryController {
    private final EntryService entryService;
    private final AnswerService answerService;

    public EntryController(EntryService entryService, AnswerService answerService) {
        this.entryService = entryService;
        this.answerService = answerService;
    }

    @GetMapping(path = "/{entry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getEntry(@AuthenticationPrincipal AppUserDetails userDetails,
                                                 @PathVariable("entry_id") Integer entryId) {
        Entry entry;

        try {
            entry = entryService.getEntry(entryId);
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(EntryResponseDto.buildFromModel(entry, userDetails.getUser(), true, null)))
                .build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getEntries(@AuthenticationPrincipal AppUserDetails userDetails,
                                                   @RequestParam Map<String, String> params) {
        List<Entry> entries;

        try {
            String query = params.get("query");
            Integer typeId = params.get("type") != null ? Integer.parseInt(params.get("type")) : null;
            Integer authorId = params.get("author") != null ? Integer.parseInt(params.get("author")) : null;
            Integer userId = params.get("favorites") != null ? userDetails.getId() : null;
            List<Integer> categoryIds = params.get("categories") != null ?
                    Arrays.stream(params.get("categories").split(",")).map(Integer::parseInt).toList() :
                    List.of();
            EntrySortMode sortMode = params.get("sort") != null ?
                    EntrySortMode.getModeByValue(Integer.parseInt(params.get("sort"))) : EntrySortMode.CREATED_DATE;

            entries = entryService.getEntries(query, typeId, authorId, userId, categoryIds, sortMode);
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(entries.stream()
                                .map(e -> EntryResponseDto.buildFromModel(e, userDetails.getUser(), false,
                                                                          null))
                                .toList())
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> createEntry(@AuthenticationPrincipal AppUserDetails userDetails,
                                                    @Valid @RequestBody EntryRequestDto entryRequestDto) {
        Entry entry;

        try {
            entry = entryService.createEntry(
                    entryRequestDto.getEntryTypeId(),
                    entryRequestDto.getTitle(),
                    entryRequestDto.getContent(),
                    entryRequestDto.getCategories().stream().map(CategoryDto::getCategoryId).toList(),
                    userDetails.getId(),
                    entryRequestDto.getImage() != null ? entryRequestDto.getImage().getFilename() : null,
                    entryRequestDto.getImage() != null ? Base64.decode(entryRequestDto.getImage().getData()) : null
            );
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(EntryResponseDto.buildFromModel(entry, userDetails.getUser(), true,
                                                                null)))
                .build();
    }

    @PutMapping(path = "/{entry_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> updateEntry(@AuthenticationPrincipal AppUserDetails userDetails,
                                                    @PathVariable("entry_id") Integer entryId,
                                                    @RequestBody EntryRequestDto entryRequestDto) {
        Entry entry;
        List<Answer> answers;

        try {
            entry = entryService.updateEntry(
                    entryId,
                    entryRequestDto.getEntryTypeId(),
                    entryRequestDto.getTitle(),
                    entryRequestDto.getContent(),
                    Optional.ofNullable(entryRequestDto.getCategories())
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(CategoryDto::getCategoryId)
                            .toList(),
                    userDetails.getId(),
                    entryRequestDto.getImage() != null ? entryRequestDto.getImage().getFilename() : null,
                    entryRequestDto.getImage() != null ? Base64.decode(entryRequestDto.getImage().getData()) : null
            );
            answers = answerService.getAnswers(entryId);
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(EntryResponseDto.buildFromModel(entry, userDetails.getUser(), true, answers)))
                .build();
    }

    @DeleteMapping(path = "/{entry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> deleteEntry(@AuthenticationPrincipal AppUserDetails userDetails,
                                                    @PathVariable("entry_id") Integer entryId) {
        try {
            this.entryService.deleteEntry(entryId, userDetails.getId());
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .build();
    }
}
