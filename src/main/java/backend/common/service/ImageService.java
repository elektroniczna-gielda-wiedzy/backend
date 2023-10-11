package backend.common.service;

import backend.common.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String createImage(String filename, byte[] data) {
        filename = filename.toLowerCase();

        int idx = filename.lastIndexOf('.');
        if (idx == -1) {
            throw new GenericServiceException("Invalid filename");
        }

        String ext = filename.substring(idx + 1);
        // TODO: Allow only specific image formats

        String newFilename = UUID.randomUUID().toString() + "." + ext;
        this.imageRepository.save(newFilename, data);
        return newFilename;
    }

    public void deleteImage(String filename) {
        try {
            this.imageRepository.delete(filename);
        } catch (Exception exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }

    public byte[] getImage(String filename) {
        try {
            return this.imageRepository.get(filename);
        } catch (Exception exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }
}
