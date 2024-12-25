package task.novisign.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import task.novisign.entity.Image;
import task.novisign.repository.ImageRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    private Image image;

    @BeforeEach
    void setUp() {
        image = Image.builder()
                .id(1L)
                .url("http://example.com/image.jpg")
                .time(LocalDateTime.now())
                .build();
    }

    @Test
    void testAddImage_whenImageIsValid_shouldReturnImage() {

        when(imageRepository.findByUrl("http://example.com/image.jpg")).thenReturn(null);
        when(imageRepository.save(any(Image.class))).thenReturn(image);


        Optional<Image> result = imageService.addImage("http://example.com/image.jpg");


        assert (result.isPresent());
        assert (result.get().getUrl().equals("http://example.com/image.jpg"));
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void testAddImage_whenImageAlreadyExists_shouldReturnExistingImage() {

        when(imageRepository.findByUrl("http://example.com/image.jpg")).thenReturn(image);


        Optional<Image> result = imageService.addImage("http://example.com/image.jpg");


        assert (result.isPresent());
        assert (result.get().getId().equals(image.getId()));
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    void testDeleteImage_whenImageExists_shouldReturnTrue() {

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));


        boolean result = imageService.deleteImage(1L);


        assert (result);
        verify(imageRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteImage_whenImageDoesNotExist_shouldReturnFalse() {

        when(imageRepository.findById(1L)).thenReturn(Optional.empty());


        boolean result = imageService.deleteImage(1L);


        assert (!result);
        verify(imageRepository, never()).deleteById(any());
    }

    @Test
    void testGetImages_whenKeywordMatchesUrl_shouldReturnImages() {

        when(imageRepository.findByUrlContaining("example")).thenReturn(List.of(image));


        List<Image> result = imageService.getImages("example");


        assert (result.size() == 1);
        assert (result.contains(image));
    }

    @Test
    void testSaveImages_whenUrlsContainNewAndExistingImages_shouldReturnAllImages() {

        when(imageRepository.findAllByUrlIn(any())).thenReturn(List.of(image));
        when(imageRepository.saveAll(new HashSet<>())).thenReturn(Collections.singletonList(image));


        List<Image> result = imageService.saveImages(List.of("http://example.com/image.jpg"));


        assert (result.size() == 1);
        verify(imageRepository, times(1)).saveAll(any());
    }
}
