package PostService.service;

import PostService.dto.CreatePostRequest;
import PostService.dto.PostDTO;
import PostService.dto.person.PersonDTO;
import PostService.dto.security.TokenData;
import PostService.mappers.PostMapper;
import PostService.models.Post;
import PostService.repository.PostRepository;
import PostService.service.feign.FriendshipService;
import PostService.service.feign.PersonService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


class PostServiceImplTest {
    private PostRepository postRepository = mock(PostRepository.class);

    private PersonService personService = mock(PersonService.class);

    private PostMapper postMapper = mock(PostMapper.class);

    private FriendshipService friendshipService = mock(FriendshipService.class);

    private TokenData tokenData = TokenData.builder()
            .token("some8-jwt9-token")
            .id(1L)
            .email("test@mail.com")
            .userName("superUser1337")
            .build();

    private ZonedDateTime time = ZonedDateTime.now();

    private PostServiceImpl postService = new PostServiceImpl(personService,postRepository, friendshipService, postMapper);

    @org.junit.jupiter.api.Test
    void createPost() {

        CreatePostRequest request = CreatePostRequest.builder()
                .text("someText")
                .publishingTime(time)
                .imageDownloadLink("someLink.com")
                .title("some title")
                .build();

        Post post = Post.builder()
                .imageDownloadLink("someLink.com")
                .publishingTime(time)
                .title("some title")
                .authorId(1L)
                .id(1L)
                .build();

        PostDTO postDTO = PostDTO.builder()
                .title(post.getTitle())
                .authorId(post.getAuthorId())
                .id(post.getId())
                .imageDownloadLink(post.getImageDownloadLink())
                .text(post.getText())
                .publishingTime(post.getPublishingTime())
                .build();

        PersonDTO personDTO = PersonDTO.builder()
                .username("superUser1337")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapper.toDTO(post)).thenReturn(postDTO);
        when(personService.getPersonDTOByUsername(tokenData.getUserName())).thenReturn(personDTO);

        postService.createPost(request,tokenData);
        assertEquals(postDTO, postService.findById(1L));
    }

    @org.junit.jupiter.api.Test
    void delete() {
        Post post = Post.builder()
                .imageDownloadLink("someLink.com")
                .publishingTime(time)
                .title("some title")
                .authorId(1L)
                .id(1L)
                .build();
        Post exceptedPost = Post.builder()
                .imageDownloadLink("someLink.com")
                .publishingTime(time)
                .title("some title")
                .authorId(1L)
                .id(1L)
                .isDelete(true)
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        postService.delete(1L);
        assertEquals(exceptedPost.getIsDelete(), post.getIsDelete());
    }

    @org.junit.jupiter.api.Test
    void getAllPosts() {
        Integer offset = 1;

        Integer limit = 10;

        Post post1 = Post.builder()
                .imageDownloadLink("someLink.com")
                .publishingTime(time)
                .title("some title")
                .authorId(1L)
                .id(1L)
                .build();
        Post post2 = Post.builder()
                .imageDownloadLink("someLink.com")
                .publishingTime(time)
                .title("some title")
                .authorId(1L)
                .id(1L)
                .build();

        List<Post> postsInDB = new ArrayList<>();
        postsInDB.add(post1);
        postsInDB.add(post2);

        PostDTO post1DTO = PostDTO.builder()
                .title(post1.getTitle())
                .authorId(post1.getAuthorId())
                .id(post1.getId())
                .imageDownloadLink(post1.getImageDownloadLink())
                .text(post1.getText())
                .publishingTime(post1.getPublishingTime())
                .build();

        PostDTO post2DTO = PostDTO.builder()
                .title(post2.getTitle())
                .authorId(post2.getAuthorId())
                .id(post2.getId())
                .imageDownloadLink(post2.getImageDownloadLink())
                .text(post2.getText())
                .publishingTime(post2.getPublishingTime())
                .build();

        List<PostDTO> expectedListOfPosts = new ArrayList<>();
        expectedListOfPosts.add(post1DTO);
        expectedListOfPosts.add(post2DTO);

        when(postRepository.findAll(PageRequest.of(offset,limit))).thenReturn(new PageImpl<>(postsInDB));
        when(postMapper.toDTO(post1)).thenReturn(post1DTO);
        when(postMapper.toDTO(post2)).thenReturn(post2DTO);

        assertEquals(expectedListOfPosts, postService.getAllPosts(offset, limit).stream().toList());

    }
    @org.junit.jupiter.api.Test
    void getAllSubscriptionPosts(){
        Integer page = 1;
        Integer limit = 10;
        List<Post> posts = new ArrayList<>();
        List<PostDTO> postsDTO = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        Post post = Post.builder()
                .imageDownloadLink("someLink.com")
                .publishingTime(time)
                .title("some title")
                .authorId(2L)
                .id(1L)
                .build();
        PostDTO postDTO = PostDTO.builder()
                .title(post.getTitle())
                .authorId(post.getAuthorId())
                .id(post.getId())
                .imageDownloadLink(post.getImageDownloadLink())
                .text(post.getText())
                .publishingTime(post.getPublishingTime())
                .build();

        ids.add(2L);
        posts.add(post);
        postsDTO.add(postDTO);

        when(friendshipService.SubscriptionsIDs(tokenData.getUserName())).thenReturn(ids);
        when(postRepository.findAllByFilter(time,2L)).thenReturn(posts);
        when(postMapper.toDTO(post)).thenReturn(postDTO);

        assertEquals(new PageImpl<>(postsDTO).stream().toList(),
                postService.getAllSubscriptionPosts(page,limit,tokenData,time).stream().toList());
    }
}