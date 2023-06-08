package backend.controllers;

import backend.model.StandardResponse;
import backend.model.dto.EntryDto;
import backend.services.EntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/entry")
public class EntryController {

    public EntryController(
            EntryService entryService
    ) {
        this.entryService = entryService;
    }

    private final EntryService entryService;

    @GetMapping()
    public ResponseEntity<StandardResponse> getEntryList(
            @RequestParam Map<String,String> params
    ) {
        return entryService.getEntryList(params);
    }

    @GetMapping("/{entry_id}")
    public ResponseEntity<StandardResponse> getEntry(
            @PathVariable("entry_id") Integer entryId) {
        return entryService.getEntry(
                entryId
        );
    }

    @PostMapping()
    public ResponseEntity<StandardResponse> createEntry(
            @RequestBody EntryDto entryDto
    ) {
        return entryService.createEntry(
                entryDto
        );
    }

    @PutMapping("/{entry_id}")
    public ResponseEntity<StandardResponse> updateEntry(
            @PathVariable("entry_id") Integer entryId,
            @RequestBody EntryDto entryDto) {
        return entryService.updateEntry(
                entryId,
                entryDto
        );
    }

    @DeleteMapping("/{entry_id}")
    public ResponseEntity<StandardResponse> deleteEntry(
            @PathVariable("entry_id") Integer entryId) {
        return entryService.deleteEntry(
                entryId
        );
    }
}
