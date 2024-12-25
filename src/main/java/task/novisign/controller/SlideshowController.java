package task.novisign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.novisign.dto.SlideshowImageDto;
import task.novisign.service.SlideshowService;

import java.util.List;

@RestController
public class SlideshowController {

    private SlideshowService slideshowService;

    @Autowired
    public SlideshowController(SlideshowService slideshowService) {
        this.slideshowService = slideshowService;
    }

    @PostMapping("addSlideshow")
    public ResponseEntity<String> addSlideshow(@RequestBody List<SlideshowImageDto> entries) {
        try {
            Long id = slideshowService.addSlideshow(entries).getId();
            return ResponseEntity.ok(String.format("Created new slideshow with id %d", id));
        } catch (Exception ignored) {
            return ResponseEntity.badRequest().body("Error while creating new slideshow");
        }
    }

    @DeleteMapping("deleteSlideshow")
    public ResponseEntity<String> deleteSlideshow(String id) {
        return null;
    }

    @GetMapping("/slideShow/{id}/slideshowOrder")
    public ResponseEntity<List<String>> slideShowOrder(@PathVariable String id) {
        return null;
    }

    @PostMapping("/slideShow/{id}/proof-of-play/{imageId}")
    public ResponseEntity<String> proofOfPlay(@PathVariable String id, @PathVariable String imageId) {
        return null;
    }
}
