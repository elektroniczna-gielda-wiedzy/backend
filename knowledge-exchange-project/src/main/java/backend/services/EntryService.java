package backend.services;

import backend.model.StandardResponse;
import backend.model.dao.*;
import backend.model.dto.CategoryDto;
import backend.model.dto.CategoryTranslationDto;
import backend.model.dto.EntryDto;
import backend.repositories.CategoryRepository;
import backend.util.ResponseUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EntryService {
    private EntityManager entityManager;
    private CategoryRepository categoryRepository;
    private SessionFactory sessionFactory;

    public EntryService(EntityManager entityManager, CategoryRepository categoryRepository, SessionFactory sessionFactory) {
        this.entityManager = entityManager;
        this.categoryRepository = categoryRepository;
        this.sessionFactory = sessionFactory;
    }

    public ResponseEntity<StandardResponse> getEntryList(Map<String, String> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntryDao> criteriaQuery = criteriaBuilder.createQuery(EntryDao.class);
        Root<EntryDao> root = criteriaQuery.from(EntryDao.class);
        List<Predicate> predicateList = new ArrayList<>();
        if (params.containsKey("query")) {
            Predicate contentContainsQuery = criteriaBuilder.like(root.get("content"), params.get("query"));
            Predicate titleContainsQuery = criteriaBuilder.like(root.get("title"), params.get("query"));
            Predicate criteriaFulfilled = criteriaBuilder.or(contentContainsQuery, titleContainsQuery);
            predicateList.add(criteriaFulfilled);
        }
        if (params.containsKey("type")) {
            Predicate typeMatchesEntry = criteriaBuilder.equal(root.get("entryType"), params.get("type"));
            predicateList.add(typeMatchesEntry);
        }
        if (params.containsKey("category_ids")) {

        }
        if (params.containsKey("author")) {
            Predicate isAuthoredBy = criteriaBuilder.equal(root.get("author"), params.get("author"));
        }
        if (params.containsKey("order")) {

        }
        if (params.containsKey("favorites")) {

        }
        return ResponseUtil.getNotImplementedResponse();
    }


    public ResponseEntity<StandardResponse> getEntry(
            Integer entryId) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> createEntry(
            EntryDto entryDto) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            EntryDao entryDao = new EntryDao();
            EntryTypeDao entryType = new EntryTypeDao();
            entryType.setEntryTypeId(entryDto.getEntryTypeId());
            entryDao.setEntryType(entryType);
            entryDao.setTitle(entryDto.getTitle());
            entryDao.setContent(entryDto.getContent());
            entryDao.setCreatedAt(Timestamp.from(Instant.now()));
            Set<CategoryDao> categories = categoryRepository.getCategoryDaosByCategoryIdIsIn(entryDto.getCategories().stream()
                    .map(CategoryDto::getCategoryId).toList());
            entryDao.setCategories(categories);
            if (entryDto.getImage() != null) {
                ImageDao imageDao = new ImageDao();
                session.persist(imageDao);
                imageDao.setImage(entryDto.getImage());
                entryDao.setImages(Set.of(imageDao));
            }
            session.persist(entryDao);
            transaction.commit();
            StandardResponse response = StandardResponse.builder()
                .success(true)
                .messages(List.of())
                .result(List.of(EntryDto.builder()
                    .entryId(entryDao.getEntryId())
                    .entryTypeId(entryDao.getEntryType().getEntryTypeId())
                    .title(entryDao.getTitle())
                    .content(entryDao.getContent())
                    .createdAt(entryDao.getCreatedAt())
                    .author(entryDto.getAuthor())
                    .categories(
                        entryDao.getCategories().stream().map(
                            categoryDao ->

                                CategoryDto.builder()
                                    .categoryId(categoryDao.getCategoryId())
                                    .categoryType(categoryDao.getCategoryId())
                                    .parentId(categoryDao.getParentCategory().getCategoryId())
                                    .names(categoryDao.getCategoryTranslations()
                                        .stream().map(
                                            categoryTranslation ->
                                                CategoryTranslationDto.builder()
                                                        .languageId(categoryTranslation.getLanguage().getLanguageId())
                                                        .translatedName(categoryTranslation.getTranslation())
                                                        .build()

                                        ).toList())
                                    .build()

                        ).toList()
                    )
                    .image(entryDao.getImages().iterator().next().getImage())
                    .build()))
                    .build();
            return ResponseEntity.status(
                    HttpStatus.CREATED
            ).body(response);
        } catch (Exception e) {
            transaction.rollback();
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(StandardResponse.builder()
                    .success(false)
                    .messages(List.of(e.getMessage()))
                    .result(List.of()).
                    build());
        }
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
