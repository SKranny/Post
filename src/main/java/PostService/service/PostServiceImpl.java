package PostService.service;

import PostService.dto.CreatePostRequest;
import PostService.dto.PostDTO;
import PostService.dto.UpdatePostRequest;
import PostService.dto.person.PersonDTO;
import PostService.dto.security.TokenData;
import PostService.mappers.PostMapper;
import PostService.models.Post;
import PostService.repository.PostRepository;
import PostService.service.feign.FriendshipService;
import PostService.service.feign.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PersonService personService;
    private final PostRepository postRepository;

    private final FriendshipService friendshipService;

    private final PostMapper postMapper;
    @Override
    public void createPost(CreatePostRequest createPostRequest, TokenData tokenData) {
        PersonDTO user = personService.getPersonDTOByUsername(tokenData.getUserName());
        Post post = Post.builder()
                .publishingTime(createPostRequest.getPublishingTime())
                .title(createPostRequest.getTitle())
                .text(createPostRequest.getText())
                .authorId(user.getId())
                .imageDownloadLink(createPostRequest.getImageDownloadLink())
                .build();
        postRepository.save(post);

    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Error! The Post was not found by id!"));
        post.setIsDelete(true);
        postRepository.save(post);
    }

    @Override
    public PostDTO findById(Long id) {
        return postMapper.toDTO(postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Error! The Post was not found by id!")));
    }

    @Override
    public PostDTO editPost(UpdatePostRequest updatePostRequest, TokenData tokenData) {
        PersonDTO user = personService.getPersonDTOByUsername(tokenData.getUserName());
        Post post = postRepository.findByIdAndAuthorId(updatePostRequest.getPostId(), user.getId())
                .orElseThrow(()-> new RuntimeException("Error! The Post was not found by id and author id!"));
        post.setTitle(updatePostRequest.getTitle());
        post.setText(updatePostRequest.getText());
        post.setImageDownloadLink(updatePostRequest.getImageDownloadLink());
        return postMapper.toDTO(postRepository.save(post));
    }

    @Override
    public Page<PostDTO> getAllPosts(Integer page, Integer limit) {
        return new PageImpl<>(postRepository.findAll(PageRequest.of(page,limit))
                .stream()
                .map(postMapper::toDTO).toList());
    }

    @Override
    public Page<PostDTO> getAllSubscriptionPosts(Integer page, Integer limit, TokenData tokenData,
                                                 ZonedDateTime publishTime) {
        List<Post> posts = new ArrayList<>();
        friendshipService.SubscriptionsIDs(tokenData.getUserName())
                .forEach(id -> posts.addAll(postRepository.findAllByFilter(checkTime(publishTime), id)));
        posts.sort(Comparator.comparing(Post::getPublishingTime).reversed());
        return new PageImpl<>(posts.stream()
                .map(postMapper::toDTO).toList(),
                PageRequest.of(page,limit),
                (long) posts.size());
    }

    private ZonedDateTime checkTime(ZonedDateTime time){
        if (time == null){
            return ZonedDateTime.now();
        }
        return time;
    }

}
