package backend.model.validators;

import backend.model.dto.UserAuthDto;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import static backend.util.ExchangeAppUtils.REQUIRED_VALUE_ERROR_FORMAT;

@Builder
public class UserAuthDtoValidator {
    private boolean emailRequired;

    private boolean passwordRequired;

    private boolean emailValidationRequired;

    public void validate(UserAuthDto userAuthDto) throws ValidationFailedException {
        List<String> failedValidations = new ArrayList<>();
        if (emailRequired && userAuthDto.getEmail() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "email"));
        }
        if (passwordRequired && userAuthDto.getPassword() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "password"));
        }
        if (emailValidationRequired && userAuthDto.getEmail() != null) {
            EmailPatternMatcher patternValidator = new EmailPatternMatcher(List.of("@student.agh.edu.pl"));
            failedValidations.addAll(patternValidator.validate(userAuthDto.getEmail()));
        }
        if (failedValidations.size() > 0) {
            throw new ValidationFailedException(failedValidations);
        }
    }
}
