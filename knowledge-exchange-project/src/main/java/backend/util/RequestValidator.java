package backend.util;

import backend.model.dto.AnswerDto;
import backend.model.dto.UserRegisterDto;

import java.util.ArrayList;
import java.util.List;

public class RequestValidator {
    public static final String EMPTY_EMAIL = "Email cannot be empty";
    public static final String EMPTY_FIRST_NAME = "First name cannot be empty";
    public static final String EMPTY_PASSWORD = "Password cannot be empty";
    public static final String EMPTY_LAST_NAME = "Last name cannot be empty";
    public static final String EMPTY_ANSWER_CONTENT = "Answer content cannot be empty";


    /**
     * validates register request for required parameters
     * @param userRegisterDto
     * @return list of errors, empty list implies successful validation
     */
    public static List<String> validateRegister(UserRegisterDto userRegisterDto) {
        List<String> errors = new ArrayList<>();
        if(userRegisterDto.getFirstName() == null) {
            errors.add(EMPTY_FIRST_NAME);
        }
        if(userRegisterDto.getLastName() == null) {
            errors.add(EMPTY_LAST_NAME);
        }
        if(userRegisterDto.getEmail() == null) {
            errors.add(EMPTY_EMAIL);
        }
        if(userRegisterDto.getPassword() == null) {
            errors.add(EMPTY_PASSWORD);
        }
        return errors;
    }

    public static List<String> validateAnswerAdding(AnswerDto answerDto) {
        List<String> errors = new ArrayList<>();
        if(answerDto.getContent() == null) {
            errors.add(EMPTY_ANSWER_CONTENT);
        }
        return errors;
    }
}
