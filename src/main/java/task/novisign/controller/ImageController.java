package task.novisign.controller;

import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import task.novisign.aspect.LogToKafka;
import task.novisign.entity.Image;
import task.novisign.entity.SlideshowImage;
import task.novisign.service.ImageService;
import task.novisign.service.SlideshowService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class ImageController {

    private static final String TOPIC = "Images";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ImageService imageService;
    private final SlideshowService slideshowService;

    @Autowired
    public ImageController(KafkaTemplate<String, String> kafkaTemplate, ImageService imageService, SlideshowService slideshowService) {
        this.kafkaTemplate = kafkaTemplate;
        this.imageService = imageService;
        this.slideshowService = slideshowService;
    }

    @LogToKafka
    @PostMapping("addImage")
    public Long uploadImage(@RequestBody String url) {
        Optional<Image> image = imageService.addImage(url);
        return image.map(Image::getId).orElse(null);
    }

    @DeleteMapping("deleteImage/{id}")
    public ResponseEntity deleteImage(@PathVariable Long id) {
        if (imageService.deleteImage(id)) {
            return ResponseEntity.ok().build();
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping("/images/search")
    public ResponseEntity<Collection<Image>> searchImages(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Image> images = imageService.getImages(keyword);
        try {
            long duration = Long.parseLong(keyword);
            List<SlideshowImage> slideshowImages = slideshowService.findBySlideshowDuration(duration);
            images.addAll(slideshowImages.stream().map(SlideshowImage::getImage).toList());

        } catch (NumberFormatException ignored) {}
        return ResponseEntity.ok(images);
    }

}
