package backend.services;

import backend.model.CategoryType;
import backend.model.dao.Category;
import backend.model.dao.Entry;
import backend.model.dao.EntryType;
import backend.model.dao.User;
import backend.repositories.CategoryRepository;
import backend.repositories.EntryRepository;
import backend.repositories.ImageRepository;
import backend.repositories.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static backend.model.dao.Entry.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class EntryService {
    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final EntryRepository entryRepository;

    public EntryService(CategoryRepository categoryRepository,
                        UserRepository userRepository,
                        ImageRepository imageRepository,
                        EntryRepository entryRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.entryRepository = entryRepository;
    }

    public Entry getEntry(Integer entryId) {
        Optional<Entry> entryDao = this.entryRepository.findById(entryId);
        if (entryDao.isEmpty()) {
            throw new GenericServiceException(String.format("Entry with id = %d does not exist", entryId));
        }

        Entry entry = entryDao.get();
        if (entry.getIsDeleted()) {
            throw new GenericServiceException(String.format("Entry with id = %d is deleted", entryId));
        }

        return entry;
    }

    public List<Entry> getEntries(String query, Integer type, Integer userId, Integer user, List<Integer> categoryIds) {
        // TODO: Implement ordering.
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
                    .filter(category -> category.getCategoryType() == CategoryType.FIELD)
                    .map(Category::getId)
                    .toList();
            List<Integer> departmentIds = categories.stream()
                    .filter(category -> category.getCategoryType() == CategoryType.DEPARTMENT)
                    .map(Category::getId)
                    .toList();

            entries = entries.stream().filter(entry -> {
                boolean matchFields = fieldIds.isEmpty() || entry.getCategories()
                        .stream()
                        .filter(category -> category.getCategoryType() == CategoryType.FIELD)
                        .anyMatch(category -> fieldIds.contains(category.getId()));
                boolean matchDepartments = departmentIds.isEmpty() || entry.getCategories()
                        .stream()
                        .filter(category -> category.getCategoryType() == CategoryType.DEPARTMENT)
                        .anyMatch(category -> departmentIds.contains(category.getId()));
                return matchFields && matchDepartments;
            }).toList();
        }

        return entries;
    }

    public Entry createEntry(Integer typeId, String title, String content, List<Integer> categoryIds, Integer userId,
                             String image) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new GenericServiceException(String.format("User with id = %d does not exist", userId));
        }
        User user = userOptional.get();

        Entry entry = new Entry();
        EntryType type = new EntryType();
        type.setId(typeId);

        entry.setType(type);
        entry.setTitle(title);
        entry.setContent(content);
        entry.setCategories(this.categoryRepository.getCategoriesByIdIsIn(categoryIds));
        entry.setAuthor(user);
        entry.setCreatedAt(Timestamp.from(Instant.now()));

        // TODO: Implement image.

        try {
            this.entryRepository.save(entry);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return entry;
    }

    public Entry updateEntry(Integer entryId, Integer typeId, String title, String content, List<Integer> categoryIds,
                             Integer userId, String image) {
        Optional<Entry> entryOptional = this.entryRepository.findById(entryId);
        if (entryOptional.isEmpty()) {
            throw new GenericServiceException(String.format("Entry with id = %d does not exist", entryId));
        }
        Entry entry = entryOptional.get();

        if (typeId != null) {
            EntryType type = new EntryType();
            type.setId(typeId);
            entry.setType(type);
        }

        if (title != null) {
            entry.setTitle(title);
        }

        if (content != null) {
            entry.setContent(content);
        }

        if (categoryIds.size() > 0) {
            entry.setCategories(this.categoryRepository.getCategoriesByIdIsIn(categoryIds));
        }

        if (userId != null) {
            Optional<User> userOptional = this.userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new GenericServiceException(String.format("User with id = %d does not exist", userId));
            }
            entry.setAuthor(userOptional.get());
        }

        // TODO: Implement image.

        try {
            this.entryRepository.save(entry);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return entry;
    }

    public void deleteEntry(Integer entryId, Integer userId) {
        Optional<Entry> entryOptional = this.entryRepository.findById(entryId);
        if (entryOptional.isEmpty()) {
            throw new GenericServiceException(String.format("Entry with id = %d does not exist", entryId));
        }
        Entry entry = entryOptional.get();

        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new GenericServiceException(String.format("User with id = %d does not exist", userId));
        }
        User user = userOptional.get();

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
