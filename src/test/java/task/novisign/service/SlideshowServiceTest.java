package task.novisign.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import task.novisign.dto.SlideshowImageDto;
import task.novisign.entity.Image;
import task.novisign.entity.Slideshow;
import task.novisign.entity.SlideshowImage;
import task.novisign.repository.SlideshowImageRepository;
import task.novisign.repository.SlideshowRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SlideshowServiceTest {

    @Mock
    private ImageService imageService;

    @Mock
    private SlideshowImageRepository slideshowImageRepository;

    @Mock
    private SlideshowRepository slideshowRepository;

    private SlideshowService slideshowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        slideshowService = new SlideshowService(imageService, slideshowImageRepository, slideshowRepository);
    }

    @Test
    void testAddSlideshowEntry() {
        SlideshowImage slideshowImage = SlideshowImage.builder()
                .id(1L)
                .duration(3000L)
                .build();
        when(slideshowImageRepository.save(any(SlideshowImage.class))).thenReturn(slideshowImage);
        SlideshowImage result = slideshowService.addSlideshowEntry(slideshowImage);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(slideshowImageRepository, times(1)).save(slideshowImage);
    }

    @Test
    void testDeleteSlideshowEntry() {
        Long id = 1L;
        doNothing().when(slideshowImageRepository).deleteById(id);
        slideshowService.deleteSlideshowEntry(id);
        verify(slideshowImageRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindBySlideshowDuration() {
        Long duration = 3000L;
        List<SlideshowImage> expectedImages = List.of(
                SlideshowImage.builder().id(1L).duration(duration).build()
        );
        when(slideshowImageRepository.findByDuration(duration)).thenReturn(expectedImages);
        List<SlideshowImage> result = slideshowService.findBySlideshowDuration(duration);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(duration, result.get(0).getDuration());
        verify(slideshowImageRepository, times(1)).findByDuration(duration);
    }

    @Test
    void testAddSlideshow() {
        List<SlideshowImageDto> slideshowImageDtos = List.of(
                new SlideshowImageDto("url1", 3000L),
                new SlideshowImageDto("url2", 2000L)
        );
        List<Image> images = List.of(
                Image.builder().id(1L).url("url1").build(),
                Image.builder().id(2L).url("url2").build()
        );
        Slideshow savedSlideshow = Slideshow.builder().id(1L).build();
        when(imageService.saveImages(any())).thenReturn(images);
        when(slideshowRepository.save(any(Slideshow.class))).thenReturn(savedSlideshow);
        when(slideshowImageRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Slideshow result = slideshowService.addSlideshow(slideshowImageDtos);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(imageService, times(1)).saveImages(any());
        verify(slideshowRepository, times(1)).save(any(Slideshow.class));
        verify(slideshowImageRepository, times(1)).saveAll(any());
    }
}
