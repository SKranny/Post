package PostService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
@Data
@Schema(description = "Пост")
@Builder
public class PostDTO {
    @Schema(description = "Идентификатор")
    private Long id;
    @Schema(description = "Идентификатор автора")
    private Long authorId;
    @Schema(description = "Заголовок поста")
    private String title;
    @Schema(description = "Текст поста")
    private String text;
    @Schema(description = "Ссылка на прикрепленное изображение")
    private String imageDownloadLink;
    @Schema(description = "Время публикации поста")
    private ZonedDateTime publishingTime;
}
