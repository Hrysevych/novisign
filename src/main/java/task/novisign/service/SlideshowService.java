package task.novisign.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.novisign.dto.SlideshowImageDto;
import task.novisign.entity.Image;
import task.novisign.entity.Slideshow;
import task.novisign.entity.SlideshowImage;
import task.novisign.repository.SlideshowImageRepository;
import task.novisign.repository.SlideshowRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SlideshowService {

    private ImageService imageService;
    private SlideshowImageRepository slideshowImageRepository;
    private SlideshowRepository slideshowRepository;


    @Autowired
    public SlideshowService(ImageService imageService,
                            SlideshowImageRepository slideshowImageRepository,
                            SlideshowRepository slideshowRepository) {
        this.imageService = imageService;
        this.slideshowImageRepository = slideshowImageRepository;
        this.slideshowRepository = slideshowRepository;
    }

    public SlideshowImage addSlideshowEntry(SlideshowImage entry) {
        return slideshowImageRepository.save(entry);
    }

    public void deleteSlideshowEntry(Long id) {
        slideshowImageRepository.deleteById(id);
    }

    public List<SlideshowImage> findBySlideshowDuration(Long slideshowDuration) {
        return slideshowImageRepository.findByDuration(slideshowDuration);
    }

    @Transactional
    public Slideshow addSlideshow(List<SlideshowImageDto> slideshowImageDtos) {
        Slideshow slideshow = slideshowRepository.save(Slideshow.builder().build());
        List<Image> images = imageService.saveImages(slideshowImageDtos.stream().map(SlideshowImageDto::url).toList());
        Map<String, Image> imageMap = images.stream().collect(Collectors.toMap(Image::getUrl, image -> image));
        List<SlideshowImage> slideshowImages = slideshowImageDtos.stream().map(dto -> SlideshowImage.builder()
                .slideshow(slideshow)
                .image(imageMap.get(dto.url()))
                .duration(dto.duration())
                .build()).toList();
        slideshowImageRepository.saveAll(slideshowImages);

        return slideshow;
    }
}
