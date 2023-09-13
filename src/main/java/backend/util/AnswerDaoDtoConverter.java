package backend.util;

import backend.model.dao.AnswerDao;
import backend.model.dto.AnswerDto;
import backend.repositories.ImageRepository;

public class AnswerDaoDtoConverter {
    public static AnswerDto convertToDto(AnswerDao answerDao) {
        return AnswerDto.builder()
                .answerId(answerDao.getId())
                .author(UserDaoDtoConverter.convertToDto(answerDao.getUser()))
                .content(answerDao.getContent())
                .createdAt(answerDao.getCreatedAt())
                .isTopAnswer(answerDao.getIsTopAnswer())
                .votes(answerDao.getVotes().size())
                .image(answerDao.getImages() != null && answerDao.getImages()
                        .stream()
                        .toList()
                        .size() > 0 ? ImageRepository.urlPrefix + answerDao.getImages()
                        .stream()
                        .toList()
                        .get(0)
                        .getImage() : null)
                .build();
    }
}
