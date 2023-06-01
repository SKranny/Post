package PostService.models;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_id_gen")
    @Column(name = "id")
    private Long id;
    private Long authorId;
    private String title;
    private String text;
    private String imageDownloadLink;
    @Builder.Default
    private Boolean isDelete = false;
    private ZonedDateTime publishingTime;
}
