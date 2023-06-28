package backend.model.validators;

import java.util.List;

public class ValidationFailedException extends Exception {
    private List<String> failedValidations;
    public ValidationFailedException(List<String> failedValidations) {
        super();
        this.failedValidations = failedValidations;
    }

    public List<String> getFailedValidations() {
        return failedValidations;
    }
}
