package task.novisign.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "slideshow_images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideshowImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "slideshow_id", nullable = false)
    private Slideshow slideshow;
    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;
    @Column
    private long duration;

}
