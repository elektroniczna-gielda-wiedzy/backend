package backend.adapter.rest.controller;

import backend.common.service.ImageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/{filename}")
    @ResponseBody
    public byte[] getImage(@PathVariable("filename") String filename) {
        try {
            return this.imageService.getImage(filename);
        } catch (Exception e) {
            return null;
        }
    }
}
