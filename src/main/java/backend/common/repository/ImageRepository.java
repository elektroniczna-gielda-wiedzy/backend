package backend.common.repository;

public interface ImageRepository {
    byte[] get(String filename);

    void save(String filename, byte[] data);

    void delete(String filename);
}
