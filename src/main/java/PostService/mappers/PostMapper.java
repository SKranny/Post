package PostService.mappers;

import PostService.dto.PostDTO;
import PostService.models.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDTO toDTO(Post post);
    Post toPost (PostDTO postDTO);
}
