package task.novisign.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "slideshow")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Slideshow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "slideshow", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SlideshowImage> slideshowImages;
}
