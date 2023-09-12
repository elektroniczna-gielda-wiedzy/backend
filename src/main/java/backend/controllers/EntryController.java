package backend.controllers;

import backend.model.AppUserDetails;
import backend.model.dto.EntryDto;
import backend.rest.common.StandardBody;
import backend.services.EntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/entry")
public class EntryController {
    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping()
    public ResponseEntity<StandardBody> getEntryList(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestParam Map<String, String> params) {
        return entryService.getEntryList(params, userDetails);
    }

    @GetMapping("/{entry_id}")
    public ResponseEntity<StandardBody> getEntry(
            @PathVariable("entry_id") Integer entryId) {
        return entryService.getEntry(entryId);
    }

    @PostMapping()
    public ResponseEntity<StandardBody> createEntry(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestBody EntryDto entryDto) {
        return entryService.createEntry(entryDto, userDetails);
    }

    @PutMapping("/{entry_id}")
    public ResponseEntity<StandardBody> updateEntry(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @PathVariable("entry_id") Integer entryId,
            @RequestBody EntryDto entryDto) {
        return entryService.updateEntry(entryId, entryDto, userDetails);
    }

    @DeleteMapping("/{entry_id}")
    public ResponseEntity<StandardBody> deleteEntry(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @PathVariable("entry_id") Integer entryId) {
        return entryService.deleteEntry(entryId, userDetails);
    }
}
