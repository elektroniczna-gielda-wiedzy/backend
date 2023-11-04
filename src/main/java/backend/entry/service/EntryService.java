package backend.entry.service;

import backend.common.model.Vote;
import backend.common.service.ImageService;
import backend.entry.model.*;
import backend.user.model.User;
import backend.entry.repository.CategoryRepository;
import backend.entry.repository.EntryRepository;
import backend.user.repository.UserRepository;
import backend.common.service.GenericServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static backend.entry.model.Entry.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class EntryService {
    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final EntryRepository entryRepository;

    private final ImageService imageService;

    public EntryService(CategoryRepository categoryRepository,
                        UserRepository userRepository,
                        EntryRepository entryRepository,
                        ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.entryRepository = entryRepository;
        this.imageService = imageService;
    }

    public Entry getEntry(Integer entryId) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        if (entry.getIsDeleted()) {
            throw new GenericServiceException(String.format("Entry with id = %d is deleted", entryId));
        }

        return entry;
    }

    public List<Entry> getEntries(String query, Integer type, Integer userId, Integer user,
                                  List<Integer> categoryIds, EntrySortMode sort) {
        List<Entry> entries = this.entryRepository.findAll(where(
                (titleContains(query).or(contentContains(query)))
                .and(hasType(type))
                .and(hasAuthor(userId))
                .and(favoriteBy(user))
                .and(isNotDeleted())
        ));

        // TODO: To rewrite.
        if (categoryIds.size() > 0) {
            Set<Category> categories = categoryRepository.getCategoriesByIdIsIn(categoryIds);
            List<Integer> fieldIds = categories.stream()
                    .filter(category -> category.getType() == CategoryType.FIELD)
                    .map(Category::getId)
                    .toList();
            List<Integer> departmentIds = categories.stream()
                    .filter(category -> category.getType() == CategoryType.DEPARTMENT)
                    .map(Category::getId)
                    .toList();

            entries = entries.stream().filter(entry -> {
                boolean matchFields = fieldIds.isEmpty() || entry.getCategories()
                        .stream()
                        .filter(category -> category.getType() == CategoryType.FIELD)
                        .anyMatch(category -> fieldIds.contains(category.getId()));
                boolean matchDepartments = departmentIds.isEmpty() || entry.getCategories()
                        .stream()
                        .filter(category -> category.getType() == CategoryType.DEPARTMENT)
                        .anyMatch(category -> departmentIds.contains(category.getId()));
                return matchFields && matchDepartments;
            }).toList();
        }

        if (sort != null) {
            Stream<Entry> entryStream = entries.stream();
            entries = switch (sort) {
                case CREATED_DATE -> entryStream.sorted(Comparator.comparing(Entry::getCreatedAt).reversed()).toList();
                case MODIFIED_DATE -> entryStream.sorted(Comparator.comparing(Entry::getUpdatedAt).reversed()).toList();
                case VOTES -> entryStream.sorted(Comparator.comparing(entry -> entry.getVotes().stream().map(Vote::getValue)
                        .reduce(0, Integer::sum), Comparator.reverseOrder()
                    )).toList();
            };
        }
        return entries;
    }

    public Entry createEntry(Integer typeId, String title, String content, List<Integer> categoryIds, Integer userId,
                             String imageFilename, byte[] imageData) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        EntryType type = EntryType.valueOf(typeId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry type with id = %d does not exist", typeId)));

        Entry entry = new Entry();

        entry.setType(type);
        entry.setTitle(title);
        entry.setContent(content);
        entry.setCategories(this.categoryRepository.getCategoriesByIdIsIn(categoryIds));
        entry.setAuthor(user);
        entry.setVotes(Set.of());
        entry.setLikedBy(Set.of());
        Timestamp createdAt = Timestamp.from(Instant.now());
        entry.setCreatedAt(createdAt);
        entry.setUpdatedAt(createdAt);

        if (imageFilename != null) {
            entry.setImage(this.imageService.createImage(imageFilename, imageData));
        }

        try {
            this.entryRepository.save(entry);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return entry;
    }

    public Entry updateEntry(Integer entryId, Integer typeId, String title, String content, List<Integer> categoryIds,
                             Integer userId, String imageFilename, byte[] imageData) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (!entry.getAuthor().getId().equals(user.getId()) && !user.getIsAdmin()) {
            throw new GenericServiceException("You are not allowed to edit this entry");
        }

        if (typeId != null) {
            EntryType type = EntryType.valueOf(typeId).orElseThrow(
                    () -> new GenericServiceException(String.format("Entry type with id = %d does not exist", typeId)));
            entry.setType(type);
        }

        if (title != null) {
            entry.setTitle(title);
        }

        if (content != null) {
            entry.setContent(content);
        }

        if (categoryIds != null && categoryIds.size() > 0) {
            entry.setCategories(this.categoryRepository.getCategoriesByIdIsIn(categoryIds));
        }

        if (imageFilename != null) {
            String currentImage = entry.getImage();
            if (currentImage != null) {
                this.imageService.deleteImage(currentImage);
            }
            entry.setImage(this.imageService.createImage(imageFilename, imageData));
        }

        entry.setUpdatedAt(Timestamp.from(Instant.now()));

        try {
            this.entryRepository.save(entry);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return entry;
    }

    public void deleteEntry(Integer entryId, Integer userId) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (entry.getIsDeleted()) {
            throw new GenericServiceException(String.format("Entry with id = %d is already deleted", entryId));
        }

        if (!entry.getAuthor().getId().equals(user.getId()) && !user.getIsAdmin()) {
            throw new GenericServiceException("You are not allowed to delete this entry");
        }

        entry.setIsDeleted(true);

        try {
            this.entryRepository.save(entry);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }
}
