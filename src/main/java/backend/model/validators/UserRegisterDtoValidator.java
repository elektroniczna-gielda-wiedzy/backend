package backend.model.validators;

import backend.model.dto.UserRegisterDto;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import static backend.util.ExchangeAppUtils.REQUIRED_VALUE_ERROR_FORMAT;

@Builder
public class UserRegisterDtoValidator {
    private boolean emailRequired;
    private boolean passwordRequired;
    private boolean emailValidationRequired;
    private boolean firstNameRequired;
    private boolean lastNameRequired;
    public void validate(UserRegisterDto userRegisterDto) throws ValidationFailedException {
        List<String> failedValidations = new ArrayList<>();
        if(emailRequired && userRegisterDto.getEmail() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "email"));
        }
        if(firstNameRequired && userRegisterDto.getFirstName() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "first name"));
        }
        if(lastNameRequired && userRegisterDto.getLastName() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "last name"));
        }
        if(passwordRequired && userRegisterDto.getPassword() == null) {
            failedValidations.add(String.format(REQUIRED_VALUE_ERROR_FORMAT, "password"));
        }
        if(emailValidationRequired && userRegisterDto.getEmail() != null) {
            EmailPatternMatcher patternValidator = new EmailPatternMatcher(List.of("@student.agh.edu.pl"));
            failedValidations.addAll(patternValidator.validate(userRegisterDto.getEmail()));
        }
        if(failedValidations.size() > 0) {
            throw new ValidationFailedException(failedValidations);
        }
    }
}
