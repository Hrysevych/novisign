package task.novisign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.novisign.entity.Image;
import task.novisign.entity.Slideshow;

import java.util.List;
import java.util.Set;

public interface SlideshowRepository extends JpaRepository<Slideshow, Long> {

}
