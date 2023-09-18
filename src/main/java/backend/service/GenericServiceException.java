package backend.service;

public class GenericServiceException extends RuntimeException {
    public GenericServiceException(String message) {
        super(message);
    }
}
