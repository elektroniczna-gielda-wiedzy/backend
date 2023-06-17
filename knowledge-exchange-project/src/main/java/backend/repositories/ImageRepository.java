package backend.repositories;

import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ImageRepository {
    public static final String urlPrefix = "http://localhost:8080/images/";
    String savePicture(String base64Data, String filename) throws IOException;
    byte[] getImage(String filename) throws IOException;
    boolean deletePicture(String filename);
}
