package backend.adapter.rest.controller;

import backend.common.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) {
        try {
            final HttpHeaders httpHeaders = new HttpHeaders();

            int idx = filename.lastIndexOf(".");
            if (idx == -1) {
                throw new Exception("Invalid filename");
            }
            String ext = filename.substring(idx + 1);

            switch (ext) {
                case "jpg" -> httpHeaders.setContentType(MediaType.IMAGE_JPEG);
                case "png" -> httpHeaders.setContentType(MediaType.IMAGE_PNG);
                default -> throw new Exception("Image format not supported");
            }

            return new ResponseEntity<byte[]>(this.imageService.getImage(filename), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return null;
        }
    }
}
