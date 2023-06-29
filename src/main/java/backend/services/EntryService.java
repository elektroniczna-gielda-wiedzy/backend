package backend.services;

import backend.model.AppUserDetails;
import backend.model.CategoryType;
import backend.model.StandardResponse;
import backend.model.dao.*;
import backend.model.dto.CategoryDto;
import backend.model.dto.EntryDto;
import backend.model.validators.EntryDtoValidator;
import backend.model.validators.ValidationFailedException;
import backend.repositories.CategoryRepository;
import backend.repositories.EntryRepository;
import backend.repositories.ImageRepository;
import backend.repositories.UserRepository;
import backend.util.EntryDaoDtoConverter;
import backend.util.ExchangeAppUtils;
import backend.util.ResponseUtil;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class EntryService {
    private CategoryRepository categoryRepository;
    private SessionFactory sessionFactory;
    private UserRepository userRepository;
    private ImageRepository imageRepository;
    private EntryRepository entryRepository;

    public EntryService(CategoryRepository categoryRepository, SessionFactory sessionFactory, UserRepository userRepository,
                        ImageRepository imageRepository, EntryRepository entryRepository) {
        this.categoryRepository = categoryRepository;
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.entryRepository = entryRepository;
    }

    public ResponseEntity<StandardResponse> getEntryList(Map<String, String> params, AppUserDetails userDetails) {
        Integer userId = userDetails.getId();

        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<EntryDao> criteriaQuery = criteriaBuilder.createQuery(EntryDao.class);
            Root<EntryDao> root = criteriaQuery.from(EntryDao.class);
            List<Predicate> predicateList = new ArrayList<>();

            Predicate isNotDeleted = criteriaBuilder.equal(root.get("isDeleted"), false);
            predicateList.add(isNotDeleted);

            if (params.containsKey("query")) {
                //TODO
                String query = "%" + params.get("query") + "%";
                Predicate contentContainsQuery = criteriaBuilder.like(root.get("content"), query);
                Predicate titleContainsQuery = criteriaBuilder.like(root.get("title"), query);
                Predicate criteriaFulfilled = criteriaBuilder.or(contentContainsQuery, titleContainsQuery);
                predicateList.add(criteriaFulfilled);
            }
            if (params.containsKey("type")) {
                Predicate typeMatchesEntry = criteriaBuilder.equal(root.get("entryType"), Integer.parseInt(params.get("type")));
                predicateList.add(typeMatchesEntry);
            }

            if (params.containsKey("author")) {
                Predicate isAuthoredBy = criteriaBuilder.equal(root.get("author"), Integer.parseInt(params.get("author")));
                predicateList.add(isAuthoredBy);
            }

            if (params.containsKey("order")) {
                if(params.get("order").equalsIgnoreCase("ASC")) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createdAt")));
                }
                if(params.get("order").equalsIgnoreCase("DESC")) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));
                }

            }

            if (params.containsKey("favorites") && Boolean.parseBoolean(params.get("favorites"))) {
                //TODO
                Join<EntryDao, UserDao> entryDaoUserDaoJoin = root.join("likedBy");

                Predicate favoPredicate = criteriaBuilder.equal(entryDaoUserDaoJoin.get("userId"), userId);
                predicateList.add(favoPredicate);
            }

            Predicate concatPredicate = predicateList.get(0);
            for (int i = 1; i < predicateList.size(); i++) {
                concatPredicate = criteriaBuilder.and(concatPredicate, predicateList.get(i));
            }
            criteriaQuery.where(concatPredicate);
            List<EntryDao> entries = session.createQuery(criteriaQuery).getResultList();

            if (params.containsKey("category_ids")) {
                List<Integer> categoriesIds = ExchangeAppUtils.convertCategoriesStrToList(params.get("category_ids"));
                Set<CategoryDao> categories = categoryRepository.getCategoryDaosByCategoryIdIsIn(categoriesIds);
                List<Integer> fieldsIds = categories.stream()
                        .filter(category -> category.getCategoryType() == CategoryType.FIELD)
                        .map(CategoryDao::getCategoryId)
                        .toList();
                List<Integer> departamentsIds = categories.stream()
                        .filter(category -> category.getCategoryType() == CategoryType.DEPARTAMENT)
                        .map(CategoryDao::getCategoryId)
                        .toList();

                entries = entries.stream().filter(entry -> {
                    boolean matchFields = fieldsIds.isEmpty() || entry.getCategories().stream()
                            .filter(category -> category.getCategoryType() == CategoryType.FIELD)
                            .anyMatch(category -> fieldsIds.contains(category.getCategoryId()));
                    boolean matchDepartaments = departamentsIds.isEmpty() || entry.getCategories().stream()
                            .filter(category -> category.getCategoryType() == CategoryType.DEPARTAMENT)
                            .anyMatch(category -> departamentsIds.contains(category.getCategoryId()));
                    return matchFields && matchDepartaments;
                }).toList();
            }

            StandardResponse response = StandardResponse.builder().success(true)
                    .messages(List.of())
                    .result(entries.stream().map(entryDao -> {
                                EntryDto result = EntryDaoDtoConverter.convertToDto(entryDao, false, false, false);
                                result.setFavorite(isEntryFavorite(userId, entryDao));
                                return result;
                            }
                    ).toList()).build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.builder()
                            .success(false)
                            .messages(List.of(e.getMessage()))
                            .result(List.of()).build());

        } finally {
            session.close();
        }

    }

    private boolean isEntryFavorite(Integer userId, EntryDao entry) {
        Session session = sessionFactory.openSession(); try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserDao> criteriaQuery = criteriaBuilder.createQuery(UserDao.class);
            Root<UserDao> userDaoRoot = criteriaQuery.from(UserDao.class);
            Predicate userIdPredicate = criteriaBuilder.equal(userDaoRoot.get("userId"), userId);
            criteriaQuery.where(userIdPredicate);
            UserDao user = session.createQuery(criteriaQuery).getSingleResult();
            return user.getFavorites().stream().anyMatch(favoEntry -> favoEntry.getEntryId().equals(entry.getEntryId()));
        } finally {
            session.close();
        }
    }



    public ResponseEntity<StandardResponse> getEntry(
            Integer entryId) {
        try {
            EntryDao dao = entryRepository.getEntryDaoByEntryId(entryId);
            if(dao == null) {
                throw new RuntimeException("Requested entry does not exist");
            }

            if (dao.getIsDeleted()) {
                throw new RuntimeException("Requested entry is deleted");
            }

            return ResponseEntity.ok(
                StandardResponse.builder()
                    .success(true)
                    .messages(List.of())
                    .result(List.of(EntryDaoDtoConverter.convertToDto(dao, true, true, true)))
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(
                StandardResponse.builder()
                    .success(false)
                    .messages(List.of(e.getMessage()))
                    .result(List.of())
                    .build()
            );
        }

    }

    public ResponseEntity<StandardResponse> createEntry(
            EntryDto entryDto, AppUserDetails userDetails) {
        try {
            EntryDtoValidator.builder()
                    .entryTypeRequired(true)
                    .contentRequired(true)
                    .categoriesRequired(true)
                    .titleRequired(true)
                    .build().validate(entryDto);
        } catch (ValidationFailedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.builder()
                            .success(false)
                            .messages(e.getFailedValidations())
                            .result(List.of()).build());
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {

            UserDao user = userRepository.findUserDaoByUserId(userDetails.getId());

            EntryDao entryDao = new EntryDao();
            EntryTypeDao entryType = new EntryTypeDao();
            entryType.setEntryTypeId(entryDto.getEntryTypeId());
            entryDao.setEntryType(entryType);
            entryDao.setTitle(entryDto.getTitle());
            entryDao.setContent(entryDto.getContent());
            entryDao.setCreatedAt(Timestamp.from(Instant.now()));
            entryDao.setAuthor(user);
            Set<CategoryDao> categories = categoryRepository.getCategoryDaosByCategoryIdIsIn(entryDto.getCategories().stream()
                    .map(CategoryDto::getCategoryId).toList());
            entryDao.setCategories(categories);

            session.persist(entryDao);
            session.flush();
            if (entryDto.getImage() != null) {
                ImageDao imageDao = new ImageDao();
                imageDao.setImage(imageRepository.savePicture(entryDto.getImage(), String.format("image-%d-%d-%d.jpg", userDetails.getId(),
                        entryDao.getEntryId(), new Random().nextInt(10000))));
                session.persist(imageDao);
                entryDao.setImages(Set.of(imageDao));
            }
            transaction.commit();
            StandardResponse response = StandardResponse.builder()
                .success(true)
                .messages(List.of())
                .result(List.of(EntryDaoDtoConverter.convertToDto(entryDao, false, true, true)))
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
        } finally {
            session.close();
        }
    }

    public ResponseEntity<StandardResponse> updateEntry(
            Integer entryId,
            EntryDto entryDto,
            AppUserDetails userDetails) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            EntryDao entryDao = entryRepository.getEntryDaoByEntryId(entryId);
            if(entryDao == null) {
                throw new RuntimeException("Requested entry does not exist");
            }

            if (entryDao.getIsDeleted()) {
                throw new RuntimeException("Requested entry is deleted");
            }

            if(!entryDao.getAuthor().getUserId().equals(userDetails.getId())
                    && !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                throw new RuntimeException("You are not allowed to update this entry");
            }

            entryDao.setTitle(Optional.ofNullable(entryDto.getTitle()).orElse(entryDao.getTitle()));
            entryDao.setContent(Optional.ofNullable(entryDto.getContent()).orElse(entryDao.getContent()));
            entryDao.setUpdatedAt(Timestamp.from(Instant.now()));
            if (entryDto.getCategories() != null) {
                Set<CategoryDao> categories = categoryRepository.getCategoryDaosByCategoryIdIsIn(entryDto.getCategories().stream()
                        .map(CategoryDto::getCategoryId).toList());
                entryDao.setCategories(categories);
            }

            if (entryDto.getImage() != null) {
                entryDao.getImages().stream().findFirst().ifPresent(oldImageDao -> {imageRepository.deletePicture(oldImageDao.getImage());
                session.remove(session.merge(oldImageDao));});

                ImageDao imageDao = new ImageDao();
                imageDao.setImage(imageRepository.savePicture(entryDto.getImage(), String.format("image-%d-%d-%d.jpg", userDetails.getId(),
                        entryDao.getEntryId(), new Random().nextInt(10000))));
                session.persist(imageDao);
                entryDao.setImages(Set.of(imageDao));
            }
            session.merge(entryDao);
            session.flush();
            transaction.commit();
            StandardResponse response = StandardResponse.builder()
                    .success(true)
                    .messages(List.of("Entry updated successfully"))
                    .result(List.of(EntryDaoDtoConverter.convertToDto(entryDao, false, true, true)))
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            transaction.rollback();
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(StandardResponse.builder()
                    .success(false)
                    .messages(List.of(e.getMessage()))
                    .result(List.of()).
                            build());
        } finally {
            session.close();
        }
    }

    public ResponseEntity<StandardResponse> deleteEntry(
            Integer entryId,
            AppUserDetails userDetails) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            EntryDao entryDao = entryRepository.getEntryDaoByEntryId(entryId);
            if(entryDao == null) {
                throw new RuntimeException("Requested entry does not exist");
            }

            if (entryDao.getIsDeleted()) {
                throw new RuntimeException("Requested entry is already deleted");
            }

            if(!entryDao.getAuthor().getUserId().equals(userDetails.getId())
                    && !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                throw new RuntimeException("You are not allowed to delete this entry");
            }
            entryDao.setIsDeleted(true);
            session.merge(entryDao);
            transaction.commit();
            return ResponseEntity.ok(
                    StandardResponse.builder()
                            .success(true)
                            .messages(List.of("Entry deleted successfully"))
                            .result(List.of())
                            .build()
            );
        } catch (Exception e) {
            transaction.rollback();
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(StandardResponse.builder()
                    .success(false)
                    .messages(List.of(e.getMessage()))
                    .result(List.of()).
                            build());
        } finally {
            session.close();
        }
    }
}
