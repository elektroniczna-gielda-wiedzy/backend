package backend.model.validators;

import backend.model.dto.EntryDto;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import static backend.util.ExchangeAppUtils.REQUIRED_VALUE_ERROR_FORMAT;

@Builder
public class EntryDtoValidator {
    private boolean entryTypeRequired;

    private boolean titleRequired;

    private boolean contentRequired;

    private boolean categoriesRequired;

    public void validate(EntryDto entryDto) throws ValidationFailedException {
        List<String> failedValidations = new ArrayList<>();
        if (entryTypeRequired && entryDto.getEntryTypeId() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "entry type id"));
        }
        if (titleRequired && entryDto.getTitle() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "title"));
        }
        if (contentRequired && entryDto.getContent() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "content"));
        }
        if (categoriesRequired && entryDto.getCategories() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "categories"));
        }
        if (failedValidations.size() > 0) {
            throw new ValidationFailedException(failedValidations);
        }
    }
}
