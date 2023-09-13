package backend.rest.entry;

import backend.model.AppUserDetails;
import backend.model.dao.EntryDao;
import backend.rest.common.Response;
import backend.rest.common.StandardBody;
import backend.rest.common.model.EntryDto;
import backend.services.EntryService;
import backend.services.GenericServiceException;
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
    public ResponseEntity<StandardBody> getEntry(
            @PathVariable("entry_id") Integer entryId) {
        EntryDao entry;

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

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getEntries(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestParam Map<String, String> params) {

        List<EntryDao> entries;

        try {
            String query = params.get("query");
            Integer typeId = params.get("type") != null ? Integer.parseInt(params.get("type")) : null;
            Integer authorId = params.get("author") != null ? Integer.parseInt(params.get("author")) : null;
            Integer userId = params.get("favorites") != null ? userDetails.getId() : null;
            List<Integer> categories = params.get("categories") != null ?
                    Arrays.stream(params.get("categories").split(",")).map(Integer::parseInt).toList() :
                    List.of();

            entries = entryService.getEntries(query, typeId, authorId, userId, categories);
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

    @PostMapping()
    public ResponseEntity<StandardBody> createEntry(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestBody backend.model.dto.EntryDto entryDto) {
        return entryService.createEntry(entryDto, userDetails);
    }

    @PutMapping("/{entry_id}")
    public ResponseEntity<StandardBody> updateEntry(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @PathVariable("entry_id") Integer entryId,
            @RequestBody backend.model.dto.EntryDto entryDto) {
        return entryService.updateEntry(entryId, entryDto, userDetails);
    }

    @DeleteMapping("/{entry_id}")
    public ResponseEntity<StandardBody> deleteEntry(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @PathVariable("entry_id") Integer entryId) {
        return entryService.deleteEntry(entryId, userDetails);
    }
}
