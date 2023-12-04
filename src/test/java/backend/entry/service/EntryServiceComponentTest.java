package backend.entry.service;

import backend.AutoDeleteTempEntry;
import backend.SpringContextRequiringTestBase;
import backend.answer.model.Answer;
import backend.entry.model.Category;
import backend.entry.model.Entry;
import backend.entry.model.EntryType;
import backend.entry.repository.EntryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EntryServiceComponentTest extends SpringContextRequiringTestBase {

    @Autowired
    private EntryService entryService;

    @Autowired
    private EntryRepository entryRepository;

    @Test
    @Order(1)
    public void createEntryTest() {
        Integer typeId = EntryType.POST.getId();
        String title = "Entrytitle";
        String content = "EntryContent";
        List<Integer> categoryIds = List.of(2, 14);
        Integer userId = 1;
        String imageFilename = null;
        byte[] imageData = null;
        Entry entry = entryService.createEntry(typeId, title, content, categoryIds, userId, imageFilename, imageData);

        Entry resultEntry =  entryRepository.findById(entry.getId()).get();


        Assertions.assertThat(resultEntry.getTitle()).isEqualTo(title);
        Assertions.assertThat(resultEntry.getContent()).isEqualTo(content);
        Assertions.assertThat(resultEntry.getAuthor().getId()).isEqualTo(1);

        entryService.deleteEntry(resultEntry.getId(), userId);
    }

    @Test
    @Order(2)
    public void testEditEntry() {
        try (AutoDeleteTempEntry tempEntryContainer = createTempMockEntry()) {
            Entry entry = tempEntryContainer.getEntry();
            String newContent = "new entry content";
            entryService.updateEntry(
                    entry.getId(),
                    entry.getType().getId(),
                    entry.getTitle(),
                    newContent,
                    entry.getCategories().stream().map(Category::getId).toList(),
                    entry.getAuthor().getId(),
                    null,
                    null
            );

            Entry updatedEntry = entryService.getEntry(entry.getId());

            Assertions.assertThat(updatedEntry.getContent()).isEqualTo(newContent);
        }
    }

    @Test
    @Order(3)
    public void deleteEntry() {
        try (AutoDeleteTempEntry tempEntryContainer = createTempMockEntry()) {
            Entry entry = tempEntryContainer.getEntry();
            entryService.deleteEntry(entry.getId(), entry.getAuthor().getId());

            Assertions.assertThatException().isThrownBy(() -> entryService.getEntry(entry.getId()));
        }
    }
}
