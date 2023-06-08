package backend.services;

import backend.model.StandardResponse;
import backend.model.dao.*;
import backend.model.dto.CategoryDto;
import backend.model.dto.EntryDto;
import backend.repositories.CategoryRepository;
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
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EntryService {
    private CategoryRepository categoryRepository;
    private SessionFactory sessionFactory;
    private UserRepository userRepository;

    public EntryService(CategoryRepository categoryRepository, SessionFactory sessionFactory, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
    }

    public ResponseEntity<StandardResponse> getEntryList(Map<String, String> params) {
        Integer userId = 1;

        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<EntryDao> criteriaQuery = criteriaBuilder.createQuery(EntryDao.class);
            Root<EntryDao> root = criteriaQuery.from(EntryDao.class);
            List<Predicate> predicateList = new ArrayList<>();
            if (params.containsKey("query")) {
                //TODO
                Predicate contentContainsQuery = criteriaBuilder.like(root.get("content"), params.get("query"));
                Predicate titleContainsQuery = criteriaBuilder.like(root.get("title"), params.get("query"));
                Predicate criteriaFulfilled = criteriaBuilder.or(contentContainsQuery, titleContainsQuery);
                predicateList.add(criteriaFulfilled);
            }
            if (params.containsKey("type")) {
                Predicate typeMatchesEntry = criteriaBuilder.equal(root.get("entryType"), Integer.parseInt(params.get("type")));
                predicateList.add(typeMatchesEntry);
            }

            if (params.containsKey("author")) {
                //TODO
                Predicate isAuthoredBy = criteriaBuilder.equal(root.get("author"), params.get("author"));
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
                Predicate favoPredicate = criteriaBuilder.and(
                        criteriaBuilder.equal(entryDaoUserDaoJoin.get("user_id"), userId),
                        criteriaBuilder.isNotEmpty(entryDaoUserDaoJoin.get("entry_id"))
                );
                predicateList.add(favoPredicate);
            }
            if(predicateList.size() != 0) {
                Predicate concatPredicate = predicateList.get(0);
                for (int i = 1; i < predicateList.size(); i++) {
                    concatPredicate = criteriaBuilder.and(concatPredicate, predicateList.get(i));
                }
                criteriaQuery.where(concatPredicate);
            }
            List<EntryDao> entries = session.createQuery(criteriaQuery).getResultList();
            if (params.containsKey("category_ids")) {
                List<Integer> categories = ExchangeAppUtils.convertCategoriesStrToList(params.get("category_ids"));
                entries = entries.stream().filter(entry ->
                        categories.stream().allMatch(category -> entry.getCategories().stream().anyMatch(entryCategory ->
                                category.equals(entryCategory.getCategoryId())))
                ).toList();
            }
            StandardResponse response = StandardResponse.builder().success(true)
                    .messages(List.of())
                    .result(entries.stream().map(entryDao -> {
                                EntryDto result = EntryDaoDtoConverter.convertToDto(entryDao);
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
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> createEntry(
            EntryDto entryDto) {
        Integer userId = 3;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {

            UserDao user = userRepository.findUserDaoByUserId(userId);

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
                .result(List.of(EntryDaoDtoConverter.convertToDto(entryDao)))
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
            EntryDto entryDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> deleteEntry(
            Integer entryId) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
