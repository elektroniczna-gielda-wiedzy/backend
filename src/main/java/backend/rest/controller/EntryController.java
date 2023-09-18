package backend.rest.controller;

import backend.model.AppUserDetails;
import backend.model.Entry;
import backend.rest.Response;
import backend.rest.StandardBody;
import backend.rest.model.common.CategoryDto;
import backend.rest.model.common.EntryDto;
import backend.service.EntryService;
import backend.service.GenericServiceException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/entry")
public class EntryController {
    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping(path = "/{entry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getEntry(@PathVariable("entry_id") Integer entryId) {
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
                .result(List.of(EntryDto.buildFromModel(entry, true, true)))
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

            entries = entryService.getEntries(query, typeId, authorId, userId, categoryIds);
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(entries.stream().map(e -> EntryDto.buildFromModel(e, false, false)).toList())
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> createEntry(@AuthenticationPrincipal AppUserDetails userDetails,
                                                    @Valid @RequestBody EntryDto entryDto) {
        Entry entry;

        try {
            entry = entryService.createEntry(
                    entryDto.getEntryTypeId(),
                    entryDto.getTitle(),
                    entryDto.getContent(),
                    entryDto.getCategories().stream().map(CategoryDto::getCategoryId).toList(),
                    userDetails.getId(),
                    "TODO"
            );
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(EntryDto.buildFromModel(entry, true, true)))
                .build();
    }

    @PutMapping(path = "/{entry_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> updateEntry(@AuthenticationPrincipal AppUserDetails userDetails,
                                                    @PathVariable("entry_id") Integer entryId,
                                                    @RequestBody EntryDto entryDto) {
        Entry entry;

        try {
            entry = entryService.updateEntry(
                    entryId,
                    entryDto.getEntryTypeId(),
                    entryDto.getTitle(),
                    entryDto.getContent(),
                    entryDto.getCategories().stream().map(CategoryDto::getCategoryId).toList(),
                    userDetails.getId(),
                    "TODO"
            );
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(EntryDto.buildFromModel(entry, true, true)))
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
