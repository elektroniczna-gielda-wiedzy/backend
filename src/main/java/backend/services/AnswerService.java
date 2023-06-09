package backend.services;

import backend.model.AppUserDetails;
import backend.model.StandardResponse;
import backend.model.dao.AnswerDao;
import backend.model.dao.EntryDao;
import backend.model.dao.ImageDao;
import backend.model.dao.UserDao;
import backend.model.dto.AnswerDto;
import backend.repositories.AnswerRepository;
import backend.repositories.EntryRepository;
import backend.repositories.ImageRepository;
import backend.repositories.UserRepository;
import backend.util.AnswerDaoDtoConverter;
import backend.util.ResponseUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class AnswerService {

    private AnswerRepository answerRepository;
    private EntryRepository entryRepository;
    private UserRepository userRepository;
    private SessionFactory sessionFactory;
    ImageRepository imageRepository;

    public AnswerService(AnswerRepository answerRepository, EntryRepository entryRepository, UserRepository userRepository, SessionFactory sessionFactory, ImageRepository imageRepository) {
        this.answerRepository = answerRepository;
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
        this.sessionFactory = sessionFactory;
        this.imageRepository = imageRepository;
    }

    public ResponseEntity<StandardResponse> getAnswerList(
            Integer entryId
            ) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> addNewAnswer(
            Integer entryId,
            AnswerDto answerDto,
            AppUserDetails userDetails
            ) {
        //List<String> errors = RequestValidator.validateAnswerAdding(answerDto);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {

            //if(errors.size() > 0) {
          //      throw new RequestValidationException("Request validation error");
            //}

            EntryDao entryDao = entryRepository.getEntryDaoByEntryId(entryId);
            AnswerDao newAnswer = new AnswerDao();
            newAnswer.setIsTopAnswer(false);

            newAnswer.setContent(answerDto.getContent());
            newAnswer.setCreatedAt(Timestamp.from(Instant.now()));
            newAnswer.setIsDeleted(false);
            newAnswer.setVotes(Set.of());
            session.persist(newAnswer);
            session.flush();
            if(answerDto.getImage() != null) {
                ImageDao imageDao = new ImageDao();
                try {
                    imageDao.setImage(imageRepository.savePicture(answerDto.getImage(), String.format("image-%d-%d-%d.jpg", userDetails.getId(),
                            entryDao.getEntryId(), new Random().nextInt(10000))));
                } catch(IOException e) {
                    //errors.add("Image could not be saved");
                    throw e;
                }
                newAnswer.setImages(Set.of(imageDao));
                session.persist(imageDao);
            }
            UserDao userDao = userRepository.findUserDaoByUserId(userDetails.getId());
            newAnswer.setUser(userDao);
            newAnswer.setEntry(entryDao);
            transaction.commit();


            return ResponseEntity.ok(
                StandardResponse.builder()
                    .success(true)
                    .messages(List.of())
                    .result(List.of(AnswerDaoDtoConverter.convertToDto(newAnswer)))
                    .build()
            );
        } catch (Exception e) {
            transaction.rollback();
            return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                    StandardResponse
                        .builder()
                        .success(false)
                        //.messages(errors)
                        .result(List.of())
                        .build()
                );
        } finally {
            session.close();
        }
    }

    public ResponseEntity<StandardResponse> editAnswer(
            Integer entryId,
            Integer answerId,
            AnswerDto answerDto
    ) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> deleteAnswer(
            Integer entryId,
            Integer answerId
    ) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
