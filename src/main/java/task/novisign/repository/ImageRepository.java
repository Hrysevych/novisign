package task.novisign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.novisign.entity.Image;

import java.util.List;
import java.util.Set;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUrlContaining(String keyword);

    Image findByUrl(String url);

    List<Image> findAllByUrlIn(List<String> urls);
}
