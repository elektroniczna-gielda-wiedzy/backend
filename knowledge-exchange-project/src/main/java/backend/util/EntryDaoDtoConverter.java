package backend.util;

import backend.model.dao.EntryDao;
import backend.model.dto.EntryDto;
import backend.repositories.ImageRepository;

import java.util.Date;

public class EntryDaoDtoConverter {
    public static EntryDto convertToDto(EntryDao entryDao, boolean includeAnswers, boolean includeContent, boolean includeImage) {


        EntryDto.EntryDtoBuilder builder = EntryDto.builder()
            .entryId(entryDao.getEntryId())
            .entryTypeId(entryDao.getEntryType().getEntryTypeId())
            .title(entryDao.getTitle());
            if (includeContent) {
                builder.content(entryDao.getContent());
            }
            builder.author(UserDaoDtoConverter.convertToDto(entryDao.getAuthor()));
            if (includeImage) {
                builder.image(entryDao.getImages() != null && entryDao.getImages().stream().toList().size() > 0?
                        ImageRepository.urlPrefix + entryDao.getImages().stream().toList().get(0).getImage():null);
            }
            builder.createdAt(new Date(entryDao.getCreatedAt().getTime()))
            .categories(entryDao.getCategories().stream().map(
                    CategoriesDaoDtoConverter::convertToDto
            ).toList());
        if(includeAnswers) {
            builder.answersList(entryDao.getAnswers().stream().map(
                    AnswerDaoDtoConverter::convertToDto
            ).toList());
        }

        return builder.build();
    }
}
