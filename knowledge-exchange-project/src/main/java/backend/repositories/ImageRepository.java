package backend.repositories;

import java.io.IOException;

public interface ImageRepository {
    public static final String urlPrefix = "http://localhost:8080/";
    String savePicture(String base64Data, String filename) throws IOException;
}
