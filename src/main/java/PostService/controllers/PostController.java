package PostService.controllers;

import PostService.dto.CreatePostRequest;
import PostService.dto.PostDTO;
import PostService.dto.UpdatePostRequest;
import PostService.dto.security.TokenAuthentication;
import PostService.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/post")
@RequiredArgsConstructor
@Tag(name="Post-service", description="Post management")
public class PostController {

    private final PostService postService;

    @Operation(summary = "Creating posts")
    @PostMapping("/create")
    public void createPost(@Valid @RequestBody @Parameter(description = "Post") CreatePostRequest createPostRequest,
                           TokenAuthentication authentication) {
        postService.createPost(createPostRequest, authentication.getTokenData());
    }
    @PutMapping("/edit")
    @Operation(summary = "Editing posts by ID")
    public PostDTO editPost(@Valid @RequestBody UpdatePostRequest req, TokenAuthentication authentication) {
        return postService.editPost(req, authentication.getTokenData());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting posts by ID")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Getting posts by ID ")
    public PostDTO getPostById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Getting all posts")
    public Page<PostDTO> getAllPosts(
            @Valid @Min(0) @RequestParam(name = "page", defaultValue = "0", required = false)Integer page,
            @Valid @Min(0) @RequestParam(name = "limit", defaultValue = "20", required = false)Integer limit){
        return postService.getAllPosts(page, limit);
    }
    @GetMapping
    @Operation(summary = "Getting all subscriptions posts")
    public Page<PostDTO> getAllSubscriptionPosts(
            @Valid @Min(0) @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @Valid @Min(0) @RequestParam(name = "limit", defaultValue = "20", required = false) Integer limit,
            @RequestParam(name = "publishTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime publishTime,
            TokenAuthentication authentication){
        return postService.getAllSubscriptionPosts(page, limit, authentication.getTokenData(), publishTime);
    }
}
