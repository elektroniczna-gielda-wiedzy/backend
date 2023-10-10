package backend.common.repository;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class FileSystemImageRepository implements ImageRepository {
    private final String basePath;

    public FileSystemImageRepository() {
        // TODO: Add basePath to constructor parameter injected from system properties.
        this.basePath = System.getProperty("user.dir") + File.separator + "images" + File.separator;
        createImagesDirIfNotExists();
    }

    @Override
    public byte[] get(String filename) {
        Path path = Paths.get(this.basePath + filename);
        byte[] data;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return data;
    }

    @Override
    public void save(String filename, byte[] data) {
        File file = new File(this.basePath + filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(String filename) {
        File file = new File(this.basePath + filename);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException(String.format("Failed to delete %s", filename));
            }
        }
    }

    private String createImagesDirIfNotExists() {
        try {
            Files.createDirectory(Path.of(this.basePath));
            return this.basePath;
        } catch (IOException e) {
            System.out.println("Failure during image dir creation. Image directory may already exist.");
        }
        return this.basePath;
    }
}
