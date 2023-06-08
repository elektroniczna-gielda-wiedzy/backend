package backend.util;

import backend.model.dao.EntryDao;
import backend.model.dto.EntryDto;

import java.util.Date;

public class EntryDaoDtoConverter {
    public static EntryDto convertToDto(EntryDao entryDao) {


        return EntryDto.builder()
            .entryId(entryDao.getEntryId())
            .entryTypeId(entryDao.getEntryType().getEntryTypeId())
            .title(entryDao.getTitle())
            .content(entryDao.getContent())
            .author(UserDaoDtoConverter.convertToDto(entryDao.getAuthor()))
            .image(entryDao.getImages() != null && entryDao.getImages().stream().toList().size() > 0?
            entryDao.getImages().stream().toList().get(0).getImage():null)
            .createdAt(new Date(entryDao.getCreatedAt().getTime()))
            .categories(entryDao.getCategories().stream().map(
                    CategoriesDaoDtoConverter::convertToDto
            ).toList()).build();
    }
}
