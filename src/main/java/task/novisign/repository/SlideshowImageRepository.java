package task.novisign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.novisign.entity.SlideshowImage;

import java.util.List;

public interface SlideshowImageRepository extends JpaRepository<SlideshowImage, Long> {

    List<SlideshowImage> findByDuration(Long slideshowDuration);
}
