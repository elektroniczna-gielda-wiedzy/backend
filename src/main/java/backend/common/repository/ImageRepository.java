package backend.common.repository;

import java.io.IOException;

public interface ImageRepository {
    String urlPrefix = "";

    String savePicture(String base64Data, String filename) throws IOException;

    byte[] getImage(String filename) throws IOException;

    boolean deletePicture(String filename);
}
