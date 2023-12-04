package backend;

import backend.entry.model.Entry;
import backend.entry.repository.EntryRepository;

public class AutoDeleteTempEntry implements AutoCloseable {

    private final Entry entry;
    private final EntryRepository entryRepository;

    public AutoDeleteTempEntry(Entry entry, EntryRepository entryRepository) {
        this.entry = entry;
        this.entryRepository = entryRepository;
    }
    @Override
    public void close() {
        entryRepository.delete(entry);
    }

    public Entry getEntry() {
        return entry;
    }
}
