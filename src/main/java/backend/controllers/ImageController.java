package backend.controllers;

import backend.repositories.ImageRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping(value = "/{image_name}", produces = IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("image_name") String imageName) {
        try {
            return imageRepository.getImage(imageName);
        } catch (Exception e) {
            System.out.println("IMAGE GET ERROR");
            return null;
        }


    }
}
