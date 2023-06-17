package backend.repositories;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ImageFileRepository implements ImageRepository {

    public ImageFileRepository() {
        createImagesDirIfNotExists();
    }

    public static final String IMAGES_PATH = System.getProperty("user.dir").replace("\\", "/") + "/images/";
    private String createImagesDirIfNotExists() {
        try {
            Files.createDirectory(Path.of(IMAGES_PATH));
            return IMAGES_PATH;
        } catch (IOException e) {
            System.out.println("Failure during image dir creation. Image directory may already exist.");
        }
        return IMAGES_PATH;
    }



    @Override
    public String savePicture(String base64Data, String filename) throws IOException {
        byte[] imgData = Base64.decode(base64Data);
        File file = new File(IMAGES_PATH + filename);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imgData);
        fos.close();
        return filename;
    }

    public byte[] getImage(String filename) throws IOException {
        Path path = Paths.get(IMAGES_PATH + filename);
        byte[] bytes = Files.readAllBytes(path);
        return bytes;
    }

    public boolean deletePicture(String filename) {
        File file = new File(IMAGES_PATH + filename);
        if(file.exists()) {
            return file.delete();
        }
        return false;
    }
}
