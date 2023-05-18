package backend.services;

import backend.model.StandardResponse;
import backend.model.dto.EntryDto;
import backend.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

    public ResponseEntity<StandardResponse> getEntryList() {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> getEntry(
            Integer entryId) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> createEntry(
            EntryDto entryDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> updateEntry(
            Integer entryId,
            EntryDto entryDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> deleteEntry(
            Integer entryId) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
