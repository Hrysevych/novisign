package task.novisign.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.novisign.entity.Image;
import task.novisign.repository.ImageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private ImageRepository repository;

    @Autowired
    public ImageService(ImageRepository repository) {
        this.repository = repository;
    }

    public Optional<Image> addImage(String url) {
        if (!isImage(url)) {
            return Optional.empty();
        }
        Image byUrl = repository.findByUrl(url);
        if (byUrl != null) {
            return Optional.of(byUrl);
        }
        return Optional.of(repository.save(Image.builder()
                .url(url)
                .time(LocalDateTime.now())
                .build()
        ));
    }

    public boolean deleteImage(Long id) {
        Optional<Image> byId = repository.findById(id);
        if (byId.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Image> getImages(String keyword) {
        return repository.findByUrlContaining(keyword);
    }

    @Transactional
    public List<Image> saveImages(List<String> urls) {
        List<Image> existingImages = new ArrayList<>(repository.findAllByUrlIn(urls));
        List<String> existingUrls = existingImages.stream().map(Image::getUrl).toList();

        List<Image> newlySaved = repository.saveAll(urls.stream()
                .filter(url -> !existingUrls.contains(url) && isImage(url))
                .map(url -> Image.builder()
                        .url(url)
                        .time(LocalDateTime.now())
                        .build())
                .toList());
        existingImages.addAll(newlySaved);
        return existingImages;
    }

    private boolean isImage(String url) {
        String urlLowerCase = url.toLowerCase();
        return urlLowerCase.endsWith(".jpg") ||
                urlLowerCase.endsWith(".png") ||
                urlLowerCase.endsWith(".gif") ||
                urlLowerCase.endsWith(".bmp") ||
                urlLowerCase.endsWith("webp");
    }
}
