package backend.services;

import backend.model.AppUserDetails;
import backend.model.CategoryType;
import backend.model.dao.*;
import backend.model.dto.CategoryDto;
import backend.model.dto.EntryDto;
import backend.model.validators.EntryDtoValidator;
import backend.model.validators.ValidationFailedException;
import backend.repositories.CategoryRepository;
import backend.repositories.EntryRepository;
import backend.repositories.ImageRepository;
import backend.repositories.UserRepository;
import backend.rest.common.StandardBody;
import backend.util.EntryDaoDtoConverter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static backend.model.dao.EntryDao.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class EntryService {
    private final CategoryRepository categoryRepository;

    private final SessionFactory sessionFactory;

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final EntryRepository entryRepository;

    public EntryService(CategoryRepository categoryRepository,
                        SessionFactory sessionFactory,
                        UserRepository userRepository,
                        ImageRepository imageRepository,
                        EntryRepository entryRepository) {
        this.categoryRepository = categoryRepository;
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.entryRepository = entryRepository;
    }

    public EntryDao getEntry(Integer entryId) {
        Optional<EntryDao> entryDao = this.entryRepository.findById(entryId);
        if (entryDao.isEmpty()) {
            throw new GenericServiceException(String.format("Entry with id = %d does not exist", entryId));
        }

        EntryDao entry = entryDao.get();
        if (entry.getIsDeleted()) {
            throw new GenericServiceException(String.format("Entry with id = %d is deleted", entryId));
        }

        return entry;
    }

    public List<EntryDao> getEntries(String query, Integer type, Integer author, Integer user,
                                     List<Integer> categories) {
        // TODO: Implement ordering.
        List<EntryDao> entries = this.entryRepository.findAll(where(
                (titleContains(query).or(contentContains(query)))
                .and(hasType(type))
                .and(hasAuthor(author))
                .and(favoriteBy(user))
        ));

        // TODO: To rewrite.
        if (categories.size() > 0) {
            Set<CategoryDao> cats = categoryRepository.getCategoryDaosByIdIsIn(categories);
            List<Integer> fieldsIds = cats.stream()
                    .filter(category -> category.getCategoryType() == CategoryType.FIELD)
                    .map(CategoryDao::getId)
                    .toList();
            List<Integer> departmentsIds = cats.stream()
                    .filter(category -> category.getCategoryType() == CategoryType.DEPARTMENT)
                    .map(CategoryDao::getId)
                    .toList();

            entries = entries.stream().filter(entry -> {
                boolean matchFields = fieldsIds.isEmpty() || entry.getCategories()
                        .stream()
                        .filter(category -> category.getCategoryType() == CategoryType.FIELD)
                        .anyMatch(category -> fieldsIds.contains(category.getId()));
                boolean matchDepartments = departmentsIds.isEmpty() || entry.getCategories()
                        .stream()
                        .filter(category -> category.getCategoryType() == CategoryType.DEPARTMENT)
                        .anyMatch(category -> departmentsIds.contains(category.getId()));
                return matchFields && matchDepartments;
            }).toList();
        }

        return entries;
    }

    public ResponseEntity<StandardBody> createEntry(EntryDto entryDto, AppUserDetails userDetails) {
        try {
            EntryDtoValidator.builder()
                    .entryTypeRequired(true)
                    .contentRequired(true)
                    .categoriesRequired(true)
                    .titleRequired(true)
                    .build()
                    .validate(entryDto);
        } catch (ValidationFailedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardBody.builder()
                                  .success(false)
                                  .messages(e.getFailedValidations())
                                  .result(List.of())
                                  .build());
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {

            UserDao user = userRepository.findById(userDetails.getId()).get();

            EntryDao entryDao = new EntryDao();
            EntryTypeDao entryType = new EntryTypeDao();
            entryType.setId(entryDto.getEntryTypeId());
            entryDao.setType(entryType);
            entryDao.setTitle(entryDto.getTitle());
            entryDao.setContent(entryDto.getContent());
            entryDao.setCreatedAt(Timestamp.from(Instant.now()));
            entryDao.setAuthor(user);
            Set<CategoryDao> categories = categoryRepository.getCategoryDaosByIdIsIn(entryDto.getCategories()
                                                                                             .stream()
                                                                                             .map(CategoryDto::getCategoryId)
                                                                                             .toList());
            entryDao.setCategories(categories);

            session.persist(entryDao);
            session.flush();
            if (entryDto.getImage() != null) {
                ImageDao imageDao = new ImageDao();
                imageDao.setImage(imageRepository.savePicture(entryDto.getImage(),
                                                              String.format("image-%d-%d-%d.jpg",
                                                                            userDetails.getId(),
                                                                            entryDao.getId(),
                                                                            new Random().nextInt(10000))));
                session.persist(imageDao);
                entryDao.setImages(Set.of(imageDao));
            }
            transaction.commit();
            StandardBody response = StandardBody.builder()
                    .success(true)
                    .messages(List.of())
                    .result(List.of(EntryDaoDtoConverter.convertToDto(entryDao, false, true, true)))
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            transaction.rollback();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardBody.builder()
                                  .success(false)
                                  .messages(List.of(e.getMessage()))
                                  .result(List.of())
                                  .build());
        } finally {
            session.close();
        }
    }

    public ResponseEntity<StandardBody> updateEntry(Integer entryId, EntryDto entryDto, AppUserDetails userDetails) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<EntryDao> entry = entryRepository.findById(entryId);
            if (entry.isEmpty()) {
                throw new RuntimeException("Requested entry does not exist");
            }
            EntryDao entryDao = entry.get();

            if (entryDao.getIsDeleted()) {
                throw new RuntimeException("Requested entry is deleted");
            }

            if (!entryDao.getAuthor().getId().equals(userDetails.getId()) && !userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority("ADMIN"))) {
                throw new RuntimeException("You are not allowed to update this entry");
            }

            entryDao.setTitle(Optional.ofNullable(entryDto.getTitle()).orElse(entryDao.getTitle()));
            entryDao.setContent(Optional.ofNullable(entryDto.getContent()).orElse(entryDao.getContent()));
            entryDao.setUpdatedAt(Timestamp.from(Instant.now()));
            if (entryDto.getCategories() != null) {
                Set<CategoryDao> categories = categoryRepository.getCategoryDaosByIdIsIn(entryDto.getCategories()
                                                                                                 .stream()
                                                                                                 .map(CategoryDto::getCategoryId)
                                                                                                 .toList());
                entryDao.setCategories(categories);
            }

            if (entryDto.getImage() != null) {
                entryDao.getImages().stream().findFirst().ifPresent(oldImageDao -> {
                    imageRepository.deletePicture(oldImageDao.getImage());
                    session.remove(session.merge(oldImageDao));
                });

                ImageDao imageDao = new ImageDao();
                imageDao.setImage(imageRepository.savePicture(entryDto.getImage(),
                                                              String.format("image-%d-%d-%d.jpg",
                                                                            userDetails.getId(),
                                                                            entryDao.getId(),
                                                                            new Random().nextInt(10000))));
                session.persist(imageDao);
                entryDao.setImages(Set.of(imageDao));
            }
            session.merge(entryDao);
            session.flush();
            transaction.commit();
            StandardBody response = StandardBody.builder()
                    .success(true)
                    .messages(List.of("Entry updated successfully"))
                    .result(List.of(EntryDaoDtoConverter.convertToDto(entryDao, false, true, true)))
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            transaction.rollback();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardBody.builder()
                                  .success(false)
                                  .messages(List.of(e.getMessage()))
                                  .result(List.of())
                                  .build());
        } finally {
            session.close();
        }
    }

    public ResponseEntity<StandardBody> deleteEntry(Integer entryId, AppUserDetails userDetails) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<EntryDao> entry = entryRepository.findById(entryId);
            if (entry.isEmpty()) {
                throw new RuntimeException("Requested entry does not exist");
            }
            EntryDao entryDao = entry.get();

            if (entryDao.getIsDeleted()) {
                throw new RuntimeException("Requested entry is already deleted");
            }

            if (!entryDao.getAuthor().getId().equals(userDetails.getId()) && !userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority("ADMIN"))) {
                throw new RuntimeException("You are not allowed to delete this entry");
            }
            entryDao.setIsDeleted(true);
            session.merge(entryDao);
            transaction.commit();
            return ResponseEntity.ok(StandardBody.builder()
                                             .success(true)
                                             .messages(List.of("Entry deleted successfully"))
                                             .result(List.of())
                                             .build());
        } catch (Exception e) {
            transaction.rollback();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardBody.builder()
                                  .success(false)
                                  .messages(List.of(e.getMessage()))
                                  .result(List.of())
                                  .build());
        } finally {
            session.close();
        }
    }
}
