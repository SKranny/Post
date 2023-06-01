package PostService.service;

import PostService.dto.CreatePostRequest;
import PostService.dto.PostDTO;
import PostService.dto.UpdatePostRequest;
import PostService.dto.security.TokenData;
import org.springframework.data.domain.Page;

import java.time.ZonedDateTime;
import java.util.List;
public interface PostService {
    void createPost(CreatePostRequest createPostRequest, TokenData tokenData);

    void delete(Long id);

    PostDTO findById(Long id);

    PostDTO editPost(UpdatePostRequest updatePostRequest, TokenData tokenData);

    Page<PostDTO> getAllPosts(Integer offset, Integer limit);

    Page<PostDTO> getAllSubscriptionPosts(Integer page, Integer limit, TokenData tokenData, ZonedDateTime publishTime);

}
