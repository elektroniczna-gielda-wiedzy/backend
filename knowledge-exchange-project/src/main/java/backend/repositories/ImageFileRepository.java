package backend.repositories;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static backend.configurations.WebConfig.IMAGES_PATH;

@Repository
public class ImageFileRepository implements ImageRepository {


    @Override
    public String savePicture(String base64Data, String filename) throws IOException {
        byte[] imgData = Base64.decode(base64Data);
        File file = new File(IMAGES_PATH + filename);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imgData);
        fos.close();
        return filename;
    }

}
