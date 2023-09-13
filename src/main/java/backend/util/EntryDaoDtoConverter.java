package backend.util;

import backend.model.dao.EntryDao;
import backend.model.dto.CategoryDto;
import backend.model.dto.EntryDto;
import backend.repositories.ImageRepository;

import java.util.Comparator;
import java.util.Date;

public class EntryDaoDtoConverter {
    public static EntryDto convertToDto(EntryDao entryDao,
                                        boolean includeAnswers,
                                        boolean includeContent,
                                        boolean includeImage) {

        EntryDto.EntryDtoBuilder builder = EntryDto.builder()
                .entryId(entryDao.getId())
                .entryTypeId(entryDao.getType().getId())
                .title(entryDao.getTitle());
        if (includeContent) {
            builder.content(entryDao.getContent());
        }
        builder.author(UserDaoDtoConverter.convertToDto(entryDao.getAuthor()));
        if (includeImage) {
            builder.image(entryDao.getImages() != null && entryDao.getImages()
                    .stream()
                    .toList()
                    .size() > 0 ? ImageRepository.urlPrefix + entryDao.getImages()
                    .stream()
                    .toList()
                    .get(0)
                    .getImage() : null);
        }
        builder.createdAt(new Date(entryDao.getCreatedAt().getTime()))
                .categories(entryDao.getCategories()
                                    .stream()
                                    .map(CategoriesDaoDtoConverter::convertToDto)
                                    .sorted(Comparator.comparingInt(CategoryDto::getCategoryId))
                                    .toList());
        if (includeAnswers) {
            builder.answersList(entryDao.getAnswers()
                                        .stream()
                                        .map(AnswerDaoDtoConverter::convertToDto)
                                        .sorted((o1, o2) -> {
                                            long res = o1.getCreatedAt().getTime() - o2.getCreatedAt().getTime();
                                            if (res < 0) {
                                                return -1;
                                            }
                                            if (res > 0) {
                                                return 1;
                                            }
                                            return 0;
                                        })
                                        .toList());
        }

        return builder.build();
    }
}
